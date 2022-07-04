package com.telegram.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telegram.dto.AccountDto;
import com.telegram.dto.CurrencyDto;
import com.telegram.entity.Type;
import com.telegram.entity.ChatEntityMongo;
import com.telegram.repo.ChatRepoMongo;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class AccountService {


    private final CrossServiceGetter crossServiceGetter;
    private final ChatRepoMongo chatRepo;

    public AccountService(CrossServiceGetter crossServiceGetter, ChatRepoMongo chatRepo) {
        this.crossServiceGetter = crossServiceGetter;
        this.chatRepo = chatRepo;
    }

    public SendMessage accountsResponse(SendMessage sendMessage) {

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        List<List<InlineKeyboardButton>> buttonList;
        buttonList = new ArrayList<>();
        buttonList.add(List.of(InlineKeyboardButton.builder().text("add account").callbackData("add account").build(),
                InlineKeyboardButton.builder().text("get accounts").callbackData("get accounts").build(),
                InlineKeyboardButton.builder().text("refactor account").callbackData("refactor account").build()));
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttonList);
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setText("choose action");

        return sendMessage;
    }

    public SendMessage addAccountForm(SendMessage sendMessage) {

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        List<CurrencyDto> currencies = crossServiceGetter.getCurrencies(sendMessage);

        sendMessage.setText("Send message with parameters of the account following the example:\n" +
                "1.title:\n" +
                "2.description:\n" +
                "3.type:(choose: CASH,BANK_ACCOUNT,BANK_DEPOSIT)\n" +
                "4.UUID of currency:\n" +
                "Currencies: " + currencies);
        return sendMessage;
    }

    public SendMessage createAccount(String message, SendMessage sendMessage) {

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        message = message.replace("Send message with parameters of the account following the example:",
                        "")
                .replace("1.title", "")
                .replace("2.description", "")
                .replace("3.type", "")
                .replace("4.UUID of currency", "")
                .replace("Currencies","")
                .replace("\n","");

        String[] split = message.split(":");


        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8090/account/");

        HashMap<String, String> jsonAccount = new HashMap<>();
        jsonAccount.put("title",split[1].trim());
        jsonAccount.put("description",split[2].trim());
        jsonAccount.put("type",split[3].trim());
        jsonAccount.put("currency",split[4].trim());

        try {
            String json = objectMapper.writeValueAsString(jsonAccount);
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).get();
            httpPost.setHeader("Authorization",chatEntity.getJwtToken());
            CloseableHttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (String.valueOf(statusCode).contains("200")) {
                sendMessage.setText("Account was successfully created");
            } else sendMessage.setText("The given data was wrong");
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sendMessage;
    }

    public SendMessage getAccounts(SendMessage sendMessage,String choice) {

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        String marker="";
        if (choice.equals("refactor account")){
            marker="update";
        }

        List<List<InlineKeyboardButton>> buttonList;
        buttonList = new ArrayList<>();
        for (AccountDto account : crossServiceGetter.getAccounts(sendMessage)) {
            buttonList.add(List.of(InlineKeyboardButton.builder()
                    .text(account.getTitle())
                    .callbackData("account "+marker+" uuid:"+account.getUuid().toString()).build()));
        }

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttonList);
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setText("choose account");



        return sendMessage;
    }

    public SendMessage getAccountsInfo (SendMessage sendMessage){

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        AtomicInteger counter= new AtomicInteger(1);
        List<CurrencyDto> currencies = crossServiceGetter.getCurrencies(sendMessage);
        Map<UUID, String> currenciesMap = currencies.stream()
                .collect(Collectors.toMap(CurrencyDto::getUuid, CurrencyDto::getTitle));
        String accounts = crossServiceGetter.getAccounts(sendMessage).stream().
                map(x -> counter.getAndIncrement()+") " +x.toString() + "" +
                        ", currency:" + currenciesMap
                        .get(x.getCurrency())).collect(Collectors.joining("\n"));
        if (accounts.equals("")){accounts="there are no accounts";}
        sendMessage.setText(accounts);
        return sendMessage;
    }

    public SendMessage getAccountInfo (SendMessage sendMessage,String uuid){

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        List<CurrencyDto> currencies = crossServiceGetter.getCurrencies(sendMessage);
        String title=null;
        String[] split = uuid.split(":");
        AccountDto account = crossServiceGetter.getAccount(UUID.fromString(split[1]),sendMessage);
        if (account==null) {sendMessage.setText("there is no such account");}
        else {
            for (CurrencyDto currency : currencies) {
                if (currency.getUuid().equals(account.getCurrency())){
                  title=currency.getTitle();
                }
            }
        }
        sendMessage.setText(account+ "" +
                ", currency:"+title);
        return sendMessage;
    }

    public SendMessage preUpdateAccount (String message, SendMessage sendMessage){

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        List<CurrencyDto> currencies = crossServiceGetter.getCurrencies(sendMessage);
        String[] split = message.split(":");
        sendMessage.setText(
                "Send parameters,which you want to update following the example.\n" +
                "If you don't want to update the parameter leave empty space.\n"+
                "\n"+
                "1.update title:\n" +
                "2.update description:\n" +
                "3.update type:(choose: CASH,BANK_ACCOUNT,BANK_DEPOSIT)\n" +
                "4.update currency:\n" +
                        "Current account:"+split[1]+
                        "\n"+
                        "\n"+
                "Currencies: " + currencies+"\n");

        return sendMessage;
    }

    public SendMessage updateAccount (String message, SendMessage sendMessage){

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()){
            sendMessage.setText("please login");
            return sendMessage;
        }

        AccountDto accountDtoGet = null;
        ObjectMapper objectMapper = new ObjectMapper();
         message = message.replace("Send parameters,which you want to update following the example.", "")
                .replace("If you don't want to update the parameter leave empty space.", "")
                .replace("1.update title", "")
                .replace("2.update description", "")
                .replace("3.update type", "")
                .replace("4.update currency", "")
                 .replace("Current account","")
                .replace("Currencies", "")
                .replace("\n","");


        String[] split = message.split(":");
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("http://localhost:8090/account/"+split[5].trim());
        ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).get();
        httpGet.setHeader("Authorization",chatEntity.getJwtToken());

        try {
            CloseableHttpResponse execute = client.execute(httpGet);
            accountDtoGet = objectMapper.readValue(execute.getEntity().getContent(), AccountDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long dtUpdate = accountDtoGet.getDtUpdate().toInstant(ZoneOffset.UTC).toEpochMilli();

        HttpPut httpPut = new HttpPut("http://localhost:8090/account/"+accountDtoGet.getUuid()+"/dt_update/"+dtUpdate);

        if (!split[1].trim().equals("")) accountDtoGet.setTitle(split[1].trim());
        if (!split[2].trim().trim().equals("")) accountDtoGet.setDescription(split[2].trim());
        if (!split[3].trim().equals("")) accountDtoGet.setType(Type.valueOf(split[3].trim()));
        if (!split[4].trim().equals("")) accountDtoGet.setCurrency(UUID.fromString(split[4].trim()));

        HashMap<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("title",accountDtoGet.getTitle());
        jsonMap.put("description",accountDtoGet.getDescription());
        jsonMap.put("type",accountDtoGet.getType());
        jsonMap.put("currency",accountDtoGet.getCurrency());

        try {
            String json = objectMapper.writeValueAsString(jsonMap);
            StringEntity entity = new StringEntity(json);
            httpPut.setEntity(entity);
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-type", "application/json");
            httpPut.setHeader("Authorization",chatEntity.getJwtToken());
            CloseableHttpResponse response = client.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            if (String.valueOf(statusCode).contains("200")) {
                sendMessage.setText("Account was successfully updated");
            } else sendMessage.setText("The given data was wrong");
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sendMessage;
    }



}
