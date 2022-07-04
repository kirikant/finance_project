package com.telegram.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telegram.dto.ReportDto;
import com.telegram.dto.ReportParamDto;
import com.telegram.entity.ChatEntityMongo;
import com.telegram.repo.ChatRepoMongo;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private CrossServiceGetter crossServiceGetter;
    @Autowired
    private ChatRepoMongo chatRepo;
    private ObjectMapper objectMapper = new ObjectMapper();

    public SendMessage reportsResponse(SendMessage sendMessage) {

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        List<List<InlineKeyboardButton>> buttonList;
        buttonList = new ArrayList<>();
        buttonList.add(List.of(InlineKeyboardButton.builder().text("add report").callbackData("add report").build(),
                InlineKeyboardButton.builder().text("get reports").callbackData("get reports").build(),
                InlineKeyboardButton.builder().text("check report").callbackData("check report").build(),
                InlineKeyboardButton.builder().text("download report").callbackData("download report").build()));
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttonList);
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setText("choose action");

        return sendMessage;
    }

    public SendMessage chooseReport(SendMessage sendMessage){

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        List<List<InlineKeyboardButton>> buttonList;
        buttonList = new ArrayList<>();
        buttonList.add(List.of(InlineKeyboardButton.builder().text("BALANCE").callbackData("BALANCE").build(),
                InlineKeyboardButton.builder().text("BY_DATE").callbackData("BY_DATE").build(),
                InlineKeyboardButton.builder().text("BY_CATEGORY").callbackData("BY_CATEGORY").build()));
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttonList);
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setText("choose report type");

        return sendMessage;
    }

    public SendMessage balanceReportForm(SendMessage sendMessage){

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        sendMessage.setText("Send message with parameters of the account following the example:\n" +
                "1.uuids of accounts:(uuid1,uuid2)\n"+
                "\n"+
                crossServiceGetter.getAccountUuids(sendMessage));

        return sendMessage;

    }

    public SendMessage byDateReportForm(SendMessage sendMessage){

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        sendMessage.setText("Send message with parameters of the account following the example:\n" +
                "1.type:BY_DATE\n" +
                "2.uuids of accounts:\n" +
                "3.uuids of categories:\n" +
                "4.from:(year-month-date)\n" +
                "5.to:(year-month-date)\n"+
                "\n"+
                crossServiceGetter.getAccountUuids(sendMessage)+
                "\n"+
                "Categories:"+crossServiceGetter.getCategories(sendMessage));

        return sendMessage;
    }

    public SendMessage byCategoryReportForm(SendMessage sendMessage){

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        sendMessage.setText("Send message with parameters of the account following the example:\n" +
                "1.type:BY_CATEGORY\n" +
                "2.uuids of accounts:\n" +
                "3.uuids of categories:\n" +
                "4.from:(year-month-date)\n" +
                "5.to:(year-month-date)\n"+
                "\n"+
                crossServiceGetter.getAccountUuids(sendMessage)+
                "\n"+
                "Categories:"+crossServiceGetter.getCategories(sendMessage));

        return sendMessage;
    }


    public SendMessage balanceReport(SendMessage sendMessage,String message){

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        message = message.replace("Send parameters,which you want to update following the example:", "")
                .replace("1.uuids of accounts", "")
                .replace("(uuid1,uuid2)", "")
                .replace("\n","");

        String[] split = message.split(":");

        List<UUID> accountUuids = Arrays.stream(split[1].split(","))
                .map(UUID::fromString).collect(Collectors.toList());
        HashMap<Object,Object> reportParamMap = new HashMap<>();
        reportParamMap.put("accounts",accountUuids);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8300/report/"+ "BALANCE");

        try {
            String json = objectMapper.writeValueAsString(reportParamMap);
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).get();
            httpPost.setHeader("Authorization",chatEntity.getJwtToken());
            CloseableHttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (String.valueOf(statusCode).contains("200")) {
                sendMessage.setText("report started creating");
            } else sendMessage.setText("The given data was wrong");
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sendMessage;
    }



    public SendMessage byDateReport(SendMessage sendMessage,String message){

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        message = message.replace("Send parameters,which you want to update following the example:", "")
                .replace("1.type", "")
                .replace("2.uuids of accounts", "")
                .replace("3.uuids of categories", "")
                .replace("4.from", "")
                .replace("5.to", "")
                .replace("\n","");

        String[] split = message.split(":");

        return specialReportCreate(sendMessage, split);
    }

    public SendMessage byCategoryReport(SendMessage sendMessage,String message){

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        message = message.replace("Send parameters,which you want to update following the example:", "")
                .replace("1.type", "")
                .replace("2.uuids of accounts", "")
                .replace("3.uuids of categories", "")
                .replace("4.from", "")
                .replace("5.to", "")
                .replace("\n","");

        String[] split = message.split(":");

        return specialReportCreate(sendMessage, split);
    }

    private SendMessage specialReportCreate(SendMessage sendMessage, String[] split) {
        List<UUID> accountUuids = Arrays.stream(split[2].split(","))
                .map(UUID::fromString).collect(Collectors.toList());
        List<UUID> categoriesUuids = Arrays.stream(split[3].split(","))
                .map(UUID::fromString).collect(Collectors.toList());
        LocalDateTime timeFrom = LocalDateTime.of( LocalDate.parse(split[4].trim()
                , DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalTime.of(0, 0));
        LocalDateTime timeTo = LocalDateTime.of( LocalDate.parse(split[5].split("Accounts uuids:")[0].trim()
                , DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalTime.of(0, 0));


        ReportParamDto reportParamDto = new ReportParamDto();
        reportParamDto.setAccounts(accountUuids);
        reportParamDto.setCategories(categoriesUuids);
        reportParamDto.setFromTime(timeFrom);
        reportParamDto.setToTime(timeTo);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8300/report/"+ split[1]);

        try {
            String json = objectMapper.writeValueAsString(reportParamDto);
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).get();
            httpPost.setHeader("Authorization",chatEntity.getJwtToken());
            CloseableHttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (String.valueOf(statusCode).contains("200")) {
                sendMessage.setText("report started creating");
            } else sendMessage.setText("The given data was wrong");
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sendMessage;
    }


    public SendMessage getReports(SendMessage sendMessage){

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        List<ReportDto> reports = crossServiceGetter.getReports(sendMessage);
        sendMessage.setText(reports.toString());

        return sendMessage;
    }
}
