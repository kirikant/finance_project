package com.schedule.service.scheduler;

import com.schedule.dto.OperationDto;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Service
public class CrossServiceGetter {

    public void checkAccountPermission(UUID accountUuid, String token) throws EntityNotFoundException {

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:8090/account/" + accountUuid);

            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("Authorization", token);
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) throw new EntityNotFoundException(response.getStatusLine().getReasonPhrase());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
