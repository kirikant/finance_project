package com.telegram.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telegram.dto.*;
import com.telegram.entity.ChatEntityMongo;
import com.telegram.repo.ChatRepoMongo;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class OperationService {

    private ObjectMapper objectMapper = new ObjectMapper();
    private final CrossServiceGetter crossServiceGetter;
    private final ChatRepoMongo chatRepo;

    public OperationService(CrossServiceGetter crossServiceGetter, ChatRepoMongo chatRepo) {
        this.crossServiceGetter = crossServiceGetter;
        this.chatRepo = chatRepo;
    }

    public SendMessage operationsResponse(SendMessage sendMessage) {

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()) {
            sendMessage.setText("please login");
            return sendMessage;
        }

        List<List<InlineKeyboardButton>> buttonList = new ArrayList<>();
        buttonList.add(List.of(InlineKeyboardButton.builder().text("add operation").callbackData("add operation").build(),
                        InlineKeyboardButton.builder().text("get operations").callbackData("get operations").build(),
                        InlineKeyboardButton.builder().text("refactor operation").callbackData("refactor operation").build(),
                InlineKeyboardButton.builder().text("delete operation").callbackData("delete operation").build())
        );

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttonList);
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setText("choose action");

        return sendMessage;
    }

    public SendMessage addOperation(SendMessage sendMessage) {

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()) {
            sendMessage.setText("please login");
            return sendMessage;
        }

        List<CurrencyDto> currencies = crossServiceGetter.getCurrencies(sendMessage);
        List<CategoryDto> categories = crossServiceGetter.getCategories(sendMessage);

        sendMessage.setText("Send message with parameters of the operation following the example:\n" +
                "1.description of the operation:\n" +
                "2.date of the operation:(year-month-date hour.minutes.seconds)\n" +
                "3.UUID of category:\n" +
                "4.value of the operation:\n" +
                "5.UUID of currency:\n" +
                "6.UUID of account:\n" +
                "Categories:" + categories + "\n" +
                "Currencies:" + currencies);
        return sendMessage;
    }

    public SendMessage preUpdateOperationForm(SendMessage sendMessage) {
        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()) {
            sendMessage.setText("please login");
            return sendMessage;
        }
        sendMessage.setText("Send message with parameters of the operation following the example:\n" +
                "uuid of operation for update:");
        return sendMessage;
    }

    public SendMessage updateOperationForm(SendMessage sendMessage, String operationUuid) {

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()) {
            sendMessage.setText("please login");
            return sendMessage;
        }

        List<CurrencyDto> currencies = crossServiceGetter.getCurrencies(sendMessage);
        List<CategoryDto> categories = crossServiceGetter.getCategories(sendMessage);

        sendMessage.setText("Send parameters,which you want to update following the example.\n" +
                "If you don't want to update the parameter leave empty space.\n" +
                "1.updated description of the operation:\n" +
                "2.updated date of the operation:(year-month-date hour.minutes.seconds)\n" +
                "3.updated UUID of category:\n" +
                "4.updated value of the operation:\n" +
                "5.updated UUID of currency:\n" +
                "Current operation:" + operationUuid +
                "\n" +
                "Categories:" + categories + "\n" +
                "Currencies:" + currencies);
        return sendMessage;
    }

    public SendMessage updateOperation(SendMessage sendMessage, String message) {

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()) {
            sendMessage.setText("please login");
            return sendMessage;
        }

        message = message.replace("Send parameters,which you want to update following the example.", "")
                .replace("If you don't want to update the parameter leave empty space.", "")
                .replace("\n", "")
                .replace("1.updated description of the operation", "")
                .replace("2.updated date of the operation", "")
                .replace("3.updated UUID of category", "")
                .replace("4.updated value of the operation", "")
                .replace("5.updated UUID of currency", "")
                .replace("Current operation", "")
                .replace("Categories", "")
                .replace("Currencies", "");

        String[] split = message.split(":");

        HashMap<String, Object> operationDto = crossServiceGetter.getOperation(sendMessage, split);
        if (operationDto == null) {
            sendMessage.setText("The given data was wrong");
            return sendMessage;
        }

        HashMap<String, Object> operation = new HashMap<>();
        operation.put("uuid", operationDto.get("uuid"));
        operation.put("dtCreate", LocalDateTime.parse(operationDto.get("dtCreate").toString())
                .toInstant(ZoneOffset.UTC).toEpochMilli());
        operation.put("dtUpdate", LocalDateTime.parse(operationDto.get("dtUpdate").toString())
                .toInstant(ZoneOffset.UTC).toEpochMilli());

        if (split[1].equals("")) {
            operation.put("description", operationDto.get("description"));
        } else operation.put("description", split[1]);

        if (split[2].equals("")) {
            operation.put("dateOperation", LocalDateTime.parse(operationDto.get("dateOperation").toString())
                    .toInstant(ZoneOffset.UTC).toEpochMilli());
        } else operation.put("dateOperation", split[2]);

        if (split[3].equals("")) {
            operation.put("category", operationDto.get("category"));
        } else operation.put("category", split[3]);

        if (split[4].equals("")) {
            operation.put("value", operationDto.get("value"));
        } else operation.put("value", split[4]);

        if (split[5].equals("")) {
            operation.put("currency", operationDto.get("currency"));
        } else operation.put("currency", split[5]);

        long epochMilli = LocalDateTime.parse(operationDto.get("dtUpdate").toString())
                .toInstant(ZoneOffset.UTC).toEpochMilli();

        HttpPut httpPut = new HttpPut("http://localhost:8090/account/" + operationDto.get("account_uuid")
                + "/operation/" + split[6].trim() + "/dt_update/" + epochMilli);
        CloseableHttpClient client = HttpClients.createDefault();

        try {
            StringEntity stringEntity = new StringEntity(objectMapper.writeValueAsString(operation));
            httpPut.setEntity(stringEntity);
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-type", "application/json");
            ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).get();
            httpPut.setHeader("Authorization", chatEntity.getJwtToken());
            CloseableHttpResponse response = client.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            if (String.valueOf(statusCode).contains("200")) {
                sendMessage.setText("operation was successfully updated");
            } else {
                sendMessage.setText("The given data was wrong");
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sendMessage;

    }


    public SendMessage createOperation(String message, SendMessage sendMessage) {

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()) {
            sendMessage.setText("please login");
            return sendMessage;
        }

        message = message.replace("1.description of the operation", "")
                .replace("2.date of the operation", "")
                .replace("3.UUID of category", "")
                .replace("4.value of the operation", "")
                .replace("5.UUID of currency", "")
                .replace("6.UUID of account", "");

        String[] split = message.split(":");
        OperationDto operationDto = new OperationDto();

        CloseableHttpClient client = HttpClients.createDefault();
        operationDto.setDescription(split[1].trim());
        operationDto.setDateOperation(LocalDateTime.parse(split[2].trim()
                , DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss")));
        operationDto.setCategory(UUID.fromString(split[3].trim()));
        operationDto.setValue(new BigDecimal(split[4].trim()));
        operationDto.setCurrency(UUID.fromString(split[5].trim()));

        HttpPost httpPost = new HttpPost("http://localhost:8090/account/" + split[6].trim() + "/operation");
        try {
            String json = objectMapper.writeValueAsString(operationDto);
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).get();
            httpPost.setHeader("Authorization", chatEntity.getJwtToken());
            CloseableHttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (String.valueOf(statusCode).contains("200")) {
                sendMessage.setText("Operation was successfully created");
            } else sendMessage.setText("The given data was wrong");
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sendMessage;
    }

    public SendMessage getOperationsForm(SendMessage sendMessage) {
        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()) {
            sendMessage.setText("please login");
            return sendMessage;
        }
        sendMessage.setText("Send message with parameters of the operation following the example:\n" +
                "1.account uuid:\n" +
                "2.date from:(year-month-date)\n" +
                "3.date to:(year-month-date)\n");
        return sendMessage;
    }


    private List<OperationDto> getOperations(SendMessage sendMessage, String message) {

        message = message.replace("Send message with parameters of the operation following the example", "")
                .replace("1.account uuid", "")
                .replace("2.date from", "")
                .replace("(year-month-date)", "")
                .replace("3.date to", "")
                .replace("\n", "");
        String[] split = message.split(":");

        ArrayList<LocalDateTime> dates = new ArrayList<>();
        LocalDateTime from = LocalDateTime.of(LocalDate.parse(split[2].trim()
                , DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalTime.of(0, 0));

        LocalDateTime to = LocalDateTime.of(LocalDate.parse(split[3].trim()
                , DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalTime.of(0, 0));
        dates.add(from);
        dates.add(to);

        return crossServiceGetter
                .getOperationsByDate(sendMessage, split, dates);
    }



    public SendMessage getOperationsInfo(SendMessage sendMessage, String message) {

        if (chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).isEmpty()) {
            sendMessage.setText("please login");
            return sendMessage;
        }

        AtomicInteger counter = new AtomicInteger(1);
        List<CurrencyDto> currencies = crossServiceGetter.getCurrencies(sendMessage);
        Map<UUID, String> currenciesMap = currencies.stream()
                .collect(Collectors.toMap(CurrencyDto::getUuid, CurrencyDto::getTitle));
        List<CategoryDto> categories = crossServiceGetter.getCategories(sendMessage);
        Map<UUID, String> categoriesMap = categories.stream()
                .collect(Collectors.toMap(CategoryDto::getUuid, CategoryDto::getTitle));
        if (getOperations(sendMessage, message) != null) {
            String operations = getOperations(sendMessage, message).stream().
                    map(x -> counter.getAndIncrement() + ") " + x.toString() + "" +
                            ", currency:" + currenciesMap
                            .get(x.getCurrency()) +
                            ", category:" + categoriesMap
                            .get(x.getCategory())).collect(Collectors.joining("\n"));
            sendMessage.setText(operations);
        } else {
            sendMessage.setText("there are no operations");
        }

        return sendMessage;
    }

    public SendMessage deleteOperationForm (SendMessage sendMessage){
        sendMessage.setText("Send message with parameters of the operation following the example:\n" +
                "1.operation uuid:\n");
        return  sendMessage;
    }

    public SendMessage deleteOperation (SendMessage sendMessage,String message){
        message=message.replace("Send message with parameters of the operation following the example:","")
                .replace("1.operation uuid","")
                .replace("\n","");


        CloseableHttpClient client = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete("http://localhost:8090/account/" +
                UUID.randomUUID()+"/operation/"+message.split(":")[1]+"/dt_update/1");
        try {
            httpDelete.setHeader("Accept", "application/json");
            httpDelete.setHeader("Content-type", "application/json");
            ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(sendMessage.getChatId()).get();
            httpDelete.setHeader("Authorization", chatEntity.getJwtToken());
            CloseableHttpResponse response = client.execute(httpDelete);
            int statusCode = response.getStatusLine().getStatusCode();
            if (String.valueOf(statusCode).contains("200")) {
                sendMessage.setText("Operation was successfully deleted");
            } else sendMessage.setText("The given data was wrong");
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sendMessage;
    }




}
