package com.telegram.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.telegram.dto.*;
import com.telegram.dto.pages.PageOfAccounts;
import com.telegram.dto.pages.PageOfCategories;
import com.telegram.dto.pages.PageOfCurrencies;
import com.telegram.dto.pages.PageOfReports;
import com.telegram.entity.ChatEntityMongo;
import com.telegram.repo.ChatRepoMongo;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CrossServiceGetter {

    private ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    private final ChatRepoMongo chatRepo;

    public CrossServiceGetter(ChatRepoMongo chatRepo) {
        this.chatRepo = chatRepo;
    }

    public List<CurrencyDto> getCurrencies(SendMessage sendMessage) {
        int page = 0;
        CloseableHttpClient client = HttpClients.createDefault();
        List<CurrencyDto> currencies = new ArrayList<>();

        try {
            while (true) {
                HttpGet httpGet = new HttpGet("http://localhost:8100/classifier/currency?page="
                        + page++ + "&size=100");
                ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).get();
                httpGet.setHeader("Authorization",chatEntity.getJwtToken());
                CloseableHttpResponse response = client.execute(httpGet);

                PageOfCurrencies pageCurrencies = objectMapper
                        .readValue(response.getEntity().getContent(), PageOfCurrencies.class);
                currencies.addAll(pageCurrencies.getContent());
                if (pageCurrencies.getLast()) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currencies;
    }

    public HashMap<String,Object> getOperation(SendMessage sendMessage, String[] split) {
        HttpGet httpGet = new HttpGet("http://localhost:8090/operation/" + split[6].trim());
        CloseableHttpClient client = HttpClients.createDefault();
        HashMap<String,Object> operationDto =null;
        try {
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).get();
            httpGet.setHeader("Authorization", chatEntity.getJwtToken());
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (String.valueOf(statusCode).contains("200")) {
                operationDto =objectMapper.readValue(response.getEntity().getContent()
                        ,new TypeReference<>(){});
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  operationDto;
    }

    public List<ReportDto> getReports(SendMessage sendMessage) {
        int page = 0;
        CloseableHttpClient client = HttpClients.createDefault();
        List<ReportDto> reports = new ArrayList<>();

        try {
            while (true) {
                HttpGet httpGet = new HttpGet("http://localhost:8300/report?page="
                        + page++ + "&size=100");
                ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).get();
                httpGet.setHeader("Authorization",chatEntity.getJwtToken());
                CloseableHttpResponse response = client.execute(httpGet);

                PageOfReports pageReports = objectMapper
                        .readValue(response.getEntity().getContent(), PageOfReports.class);
                reports.addAll(pageReports.getContent());
                if (pageReports.getLast()) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reports;
    }

    public List<CategoryDto> getCategories(SendMessage sendMessage) {
        int page = 0;
        CloseableHttpClient client = HttpClients.createDefault();
        List<CategoryDto> categories = new ArrayList<>();

        try {
            while (true) {
                HttpGet httpGet = new HttpGet("http://localhost:8100/classifier/operation/category?page="
                        + page++ + "&size=100");
                ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).get();
                httpGet.setHeader("Authorization",chatEntity.getJwtToken());
                CloseableHttpResponse response = client.execute(httpGet);

                PageOfCategories pageCategories = objectMapper
                        .readValue(response.getEntity().getContent(), PageOfCategories.class);
                categories.addAll(pageCategories.getContent());
                if (pageCategories.getLast()) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categories;
    }



    public List<AccountDto> getAccounts(SendMessage sendMessage) {
        int page = 0;
        CloseableHttpClient client = HttpClients.createDefault();
        List<AccountDto> accounts = new ArrayList<>();

        try {
            while (true) {
                HttpGet httpGet = new HttpGet("http://localhost:8090/account?page="
                        + page++ + "&size=100");
                ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).get();
                httpGet.setHeader("Authorization",chatEntity.getJwtToken());
                CloseableHttpResponse response = client.execute(httpGet);

                PageOfAccounts pageAccounts = objectMapper
                        .readValue(response.getEntity().getContent(), PageOfAccounts.class);
                accounts.addAll(pageAccounts.getContent());
                if (pageAccounts.getLast()) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public AccountDto getAccount(UUID uuid,SendMessage sendMessage) {
        List<AccountDto> accounts = getAccounts(sendMessage);
        return accounts.stream().filter(x -> x.getUuid().equals(uuid)).findFirst().get();
    }



    public boolean validateJwt(String token) {
        boolean isValid = false;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8120/user/validation");
        httpGet.setHeader("Authorization", token);
        try {
         if   (client.execute(httpGet).getStatusLine().getStatusCode()==200)  isValid=true;
            client.close();
        } catch (IOException e) {

        }
        return isValid;
    }

    public List<OperationDto> getOperationsByDate(SendMessage sendMessage, String[] split, ArrayList<LocalDateTime> dates) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8090/account/" + split[1].trim() + "/operation/by-date");
        List<OperationDto> operationDtos=null;
        try {
            String json = objectMapper.writeValueAsString(dates);
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).get();
            httpPost.setHeader("Authorization", chatEntity.getJwtToken());
            CloseableHttpResponse response = client.execute(httpPost);
            client.close();

             operationDtos = objectMapper
                    .readValue(response.getEntity().getContent(),
                            new TypeReference<>() {
                            });


        } catch (IOException e) {
            e.printStackTrace();
        }
        return operationDtos;
    }

    public String getAccountUuids(SendMessage sendMessage){
        List<AccountDto> accounts = getAccounts(sendMessage);
       return "Accounts uuids:"+accounts.stream().map(x->x.getTitle()+":"+x.getUuid())
               .collect(Collectors.joining(",\n"));
    }



}
