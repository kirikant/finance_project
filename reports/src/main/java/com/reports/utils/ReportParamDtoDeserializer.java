package com.reports.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.reports.dto.ReportParamDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

public class ReportParamDtoDeserializer extends StdDeserializer<ReportParamDto> {

    public ReportParamDtoDeserializer() {
        this(null);
    }

    public ReportParamDtoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ReportParamDto deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ReportParamDto reportParamDto = new ReportParamDto();
        JsonNode node = jp.getCodec().readTree(jp);
        ObjectMapper objectMapper = new ObjectMapper();

        reportParamDto.setAccounts(List.of(objectMapper
                .readValue(node.get("accounts").traverse(), UUID[].class)));

        if (node.get("from") != null) reportParamDto.setFromTime(LocalDateTime
                .ofInstant(Instant.ofEpochMilli(node.get("from").asLong()), ZoneOffset.UTC));

        if (node.get("to") != null) reportParamDto.setToTime(LocalDateTime
                .ofInstant(Instant.ofEpochMilli(node.get("to").asLong()), ZoneOffset.UTC));

        if (node.get("categories") != null) reportParamDto.setCategories(List.of(objectMapper
                .readValue(node.get("categories").traverse(), UUID[].class)));

        return reportParamDto;
    }
}
