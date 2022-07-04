package com.telegram.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telegram.dto.UserDto;
import com.telegram.entity.ChatEntityMongo;
import com.telegram.repo.ChatRepoMongo;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final CrossServiceGetter crossServiceGetter;
    private final ChatRepoMongo chatRepo;
    private ObjectMapper objectMapper = new ObjectMapper();

    public UserService(CrossServiceGetter crossServiceGetter, ChatRepoMongo chatRepo) {
        this.crossServiceGetter = crossServiceGetter;
        this.chatRepo = chatRepo;
    }

    public Boolean validate(String chatId) {
        boolean isValid = false;
        if (chatRepo.findChatEntityMongoByChatId(chatId).isPresent()) {
            String jwtToken = chatRepo.findChatEntityMongoByChatId(chatId).get().getJwtToken();
            isValid = crossServiceGetter.validateJwt(jwtToken);
        }
        return isValid;
    }

    public SendMessage registerForm(SendMessage sendMessage) {
        sendMessage.setText("Send message with parameters of the operation following the example:\n" +
                "login:\n" +
                "password:\n" +
                "email:");
        return sendMessage;
    }

    public SendMessage loginForm(SendMessage sendMessage) {
        sendMessage.setText("Send message with parameters of the operation following the example:\n" +
                "login:\n" +
                "password:");
        return sendMessage;
    }

    public void register(String chatId, String message, SendMessage sendMessage) {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto userDto = new UserDto();
        message = message
                .replace("Send message with parameters of the operation following the example:", "")
                .replace("login", "")
                .replace("password", "")
                .replace("email", "")
                .replace("\n", "");
        String[] split = message.split(":");
        userDto.setLogin(split[1].trim());
        userDto.setPassword(split[2].trim());
        userDto.setEmail(split[3].trim());

        if (split[3]==null){sendMessage.setText("please enter email");
        return;}
        if (split[1]==null){sendMessage.setText("please enter login");
            return;}
        if (split[2]==null){sendMessage.setText("please enter password");
            return;}

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8120/user/register");
        try {
            String json = objectMapper.writeValueAsString(userDto);
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                sendMessage.setText("User was successfully created");
                if (chatRepo.findChatEntityMongoByChatId(chatId).isPresent()){
                    ChatEntityMongo chatEntityMongo = chatRepo.findChatEntityMongoByChatId(chatId).get();
                    chatRepo.save(chatEntityMongo);
                }else {ChatEntityMongo chatEntity = new ChatEntityMongo();
                    chatEntity.setChatId(chatId);
                    chatRepo.save(chatEntity);}

            } else {
                sendMessage.setText("Such user is already existed");
            }
            client.close();
        } catch (IOException e) {

        }
    }

    public void login(String chatId, String message, SendMessage sendMessage) {
        if (chatRepo.findChatEntityMongoByChatId(chatId).isPresent()){
            UserDto userDto = new UserDto();
            message = message
                    .replace("Send message with parameters of the operation following the example:", "")
                    .replace("login", "")
                    .replace("password", "")
                    .replace("\n", "");
            String[] split = message.split(":");
            userDto.setLogin(split[1].trim());
            userDto.setPassword(split[2].trim());

            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://localhost:8120/user/login");
            try {
                String json = objectMapper.writeValueAsString(userDto);
                StringEntity entity = new StringEntity(json);
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                CloseableHttpResponse response = client.execute(httpPost);
                if (response.getStatusLine().getStatusCode() == 200) {
                    sendMessage.setText("You login successfully");
                    try (BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(response.getEntity().getContent()))) {
                        String jwt = bufferedReader.lines().collect(Collectors.joining());

                        if (chatRepo.findChatEntityMongoByChatId(chatId).isPresent()){
                            ChatEntityMongo chatEntity1 = chatRepo.findChatEntityMongoByChatId(chatId).get();
                            chatEntity1.setJwtToken(jwt);
                            chatRepo.save(chatEntity1);}
                        else {
                            ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(chatId).get();
                            chatEntity.setJwtToken(jwt);
                            chatRepo.save(chatEntity);}
                    }
                } else {
                    sendMessage.setText("The given data was wrong");
                }
                client.close();
            } catch (IOException e) {
            }
        }

    }

    public void logout(String chatId, SendMessage sendMessage){
        ChatEntityMongo chatEntity = chatRepo.findChatEntityMongoByChatId(chatId).get();
        chatEntity.setJwtToken(null);
        chatRepo.save(chatEntity);
        sendMessage.setText("You are successfully logout");
    }


}
