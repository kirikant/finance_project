package mailScheduler.service;

import mailScheduler.service.scheduler.ScheduleJob;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Service
public class CrossServiceGetter {

    private Logger logger = LoggerFactory.getLogger(CrossServiceGetter.class);

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
             logger.error("post method(http://localhost:8090/account/....) was not executed \n" +
                    e.getCause());
            e.printStackTrace();
        }

    }

}
