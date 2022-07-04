package com.schedule.service.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.schedule.dto.OperationDto;
import com.schedule.dto.ScheduleDto;
import com.schedule.dto.ScheduledOperationDto;
import com.schedule.entity.ScheduledOperationEntity;
import com.schedule.repostories.ScheduledOperationRepo;
import com.schedule.utils.Mapper;
import com.schedule.utils.ScheduleDtoSerializer;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.UUID;


public class ScheduleJob implements Job {

    private final ScheduledOperationRepo scheduledOperationRepo;
    private final Mapper mapper;
    private final ObjectMapper objectMapper;

    public ScheduleJob(ScheduledOperationRepo scheduledOperationRepo,
                       Mapper mapper, ObjectMapper objectMapper) {
        this.scheduledOperationRepo = scheduledOperationRepo;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException, EntityNotFoundException {
        try {
            String token = (String) context.getJobDetail().getJobDataMap().get("token");
            ScheduledOperationEntity scheduledOperation = scheduledOperationRepo.findById(UUID
                            .fromString((String) context.getJobDetail().getJobDataMap().get("uuid")))
                    .orElseThrow(() -> new EntityNotFoundException("There is no such scheduled operation"));
            UUID accountUuid = scheduledOperation.getOperationEntity().getAccountUuid();

            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://localhost:8090/account/"+accountUuid+"/operation");

            OperationDto operationDto = mapper.map(scheduledOperation
                    .getOperationEntity(), OperationDto.class);

            String json = objectMapper.writeValueAsString(operationDto);

            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Authorization", token);
            CloseableHttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode!=200) throw new EntityNotFoundException(response.getStatusLine().getReasonPhrase());

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
