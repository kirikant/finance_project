package com.reports.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reports.dto.*;
import com.reports.dto.pages.PageOfCategories;
import com.reports.dto.pages.PageOfCurrencies;
import com.reports.service.api.ICrossServiceGetter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class CrossServiceGetter implements ICrossServiceGetter {

    @Value("${size}")
    private int size;
    private final ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(CrossServiceGetter.class);


    public CrossServiceGetter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<AccountDto> getSortedAccountDtos(ReportParamDto reportParamDto,String token) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        StringEntity params = new StringEntity(objectMapper
                .writeValueAsString(reportParamDto.getAccounts()));

        HttpPost httpPost = new HttpPost("http://localhost:8090/account/sorted");
        httpPost.setEntity(params);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Authorization",token);
        CloseableHttpResponse response = client.execute(httpPost);
        List<AccountDto> sortedAccounts = objectMapper
                .readValue(response.getEntity().getContent(), new TypeReference<>() {
                });

        return sortedAccounts;
    }

    public List<OperationDto> getOperations(AccountDto accountDto, List<UUID> categories, String token) throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://localhost:8090/account/"
                +accountDto.getUuid()
                +"/operation/sorted");
        StringEntity params = new StringEntity(objectMapper
                .writeValueAsString(categories));
        httpPost.setEntity(params);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Authorization",token);
        CloseableHttpResponse response = client.execute(httpPost);
        List<OperationDto> sortedOperations = objectMapper
                .readValue(response.getEntity().getContent(), new TypeReference<>() {
                });
        return sortedOperations;
    }

    public List<CategoryDto> getCategoriesDtos(String token) throws IOException {
        int page = 0;
        List<CategoryDto> categories = new ArrayList<>();
        CloseableHttpClient client = HttpClients.createDefault();

        while (true) {
            HttpGet httpGet = new HttpGet("http://localhost:8100/classifier/operation/category?page="
                    + page++ + "&size=" + size);
            httpGet.setHeader("Authorization",token);
            CloseableHttpResponse response = client.execute(httpGet);

            PageOfCategories pageCategories = objectMapper
                    .readValue(response.getEntity().getContent(), PageOfCategories.class);
            categories.addAll(pageCategories.getContent());
            if (pageCategories.getLast()) break;
        }

        return categories;
    }

    public List<CurrencyDto> getCurrencyDtos(String token) throws IOException {
        int page = 0;
        List<CurrencyDto> currencies = new ArrayList<>();
        CloseableHttpClient client = HttpClients.createDefault();

        while (true) {
            HttpGet httpGet = new HttpGet("http://localhost:8100/classifier/currency?page="
                    + page++ + "&size=" + size);
            httpGet.setHeader("Authorization",token);
            CloseableHttpResponse response = client.execute(httpGet);

            PageOfCurrencies pageCurrencies = objectMapper
                    .readValue(response.getEntity().getContent(), PageOfCurrencies.class);
            currencies.addAll(pageCurrencies.getContent());
            if (pageCurrencies.getLast()) break;
        }

        return currencies;
    }

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
            logger.error(e.getCause().toString());
        }

    }
}
