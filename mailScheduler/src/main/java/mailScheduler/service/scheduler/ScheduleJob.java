package mailScheduler.service.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import mailScheduler.dto.ReportParamDto;
import mailScheduler.entity.ReportParamEntity;
import mailScheduler.entity.ScheduledReportEntity;
import mailScheduler.repositories.ScheduledReportRepo;
import mailScheduler.util.Mapper;
import mailScheduler.util.convertors.RepParEntityToDto;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.UUID;


public class ScheduleJob implements Job {

    private final ScheduledReportRepo scheduledReportRepo;
    private final Mapper mapper;
    private final ObjectMapper objectMapper;
    private final RepParEntityToDto repParEntityToDto;
    private Logger logger = LoggerFactory.getLogger(ScheduleJob.class);


    public ScheduleJob(ScheduledReportRepo scheduledReportRepo,
                       Mapper mapper, ObjectMapper objectMapper,
                       RepParEntityToDto repParEntityToDto) {
        this.scheduledReportRepo = scheduledReportRepo;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
        this.repParEntityToDto = repParEntityToDto;
    }

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException, EntityNotFoundException {
        try {
            String token = (String) context.getJobDetail().getJobDataMap().get("token");
            ScheduledReportEntity scheduledReportEntity = scheduledReportRepo.findById(UUID
                            .fromString((String) context.getJobDetail().getJobDataMap().get("uuid")))
                    .orElseThrow(() -> new EntityNotFoundException("There is no such scheduled operation"));
            ReportParamEntity reportParamEntity = scheduledReportEntity.getReportParamEntity();

            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://localhost:8300/report/" + scheduledReportEntity.getReportType());

            ReportParamDto reportParamDto = repParEntityToDto.convert(reportParamEntity);

            String json = objectMapper.writeValueAsString(reportParamDto);

            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Authorization", token);
            CloseableHttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) throw new EntityNotFoundException(response.getStatusLine().getReasonPhrase());

            client.close();
        } catch (IOException e) {
            logger.error("post method(http://localhost:8300/report/....) was not executed \n" +
                    e.getCause());
            e.printStackTrace();
        }
    }


}
