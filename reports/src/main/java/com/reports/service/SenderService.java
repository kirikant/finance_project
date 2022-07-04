package com.reports.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reports.dto.MessageDto;
import io.jsonwebtoken.Jwts;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class SenderService {

    private final ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(CrossServiceGetter.class);
    @Value("${jwt.secret}")
    String secret;

    public SenderService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void sendMail(MessageDto messageDto, String token) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost("http://localhost:8079/mail/message");
            StringEntity params = new StringEntity(objectMapper
                    .writeValueAsString(messageDto));
            httpPost.setEntity(params);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Authorization", token);
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) throw
                    new IllegalArgumentException(response.getStatusLine().getReasonPhrase());
        } catch (IOException e) {
            logger.error(e.getCause().toString());
        }
    }

    public MessageDto createMessage(File report, String description, String token, UUID uuid) {
        MessageDto messageDto = new MessageDto();
        String email = Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
                .getBody().get("email", String.class);
        messageDto.setReceiver(email);
        messageDto.setText(description);
        messageDto.setFileName(uuid);
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(report))) {
            byte[] bytes = inputStream.readAllBytes();
            messageDto.setByteFile(bytes);
        } catch (IOException e) {
            logger.error(e.getCause().toString());
        }

        return messageDto;
    }


}
