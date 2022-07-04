package com.telegram.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.telegram.dto.AccountDto;
import com.telegram.dto.ReportDto;
import com.telegram.dto.ReportParamDto;
import com.telegram.entity.ReportType;
import com.telegram.entity.Status;
import com.telegram.entity.Type;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.UUID;

public class ReportDtoDeserializer extends StdDeserializer<ReportDto> {

    public ReportDtoDeserializer() {
        this(null);
    }

    public ReportDtoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ReportDto deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ReportDto reportDto = new ReportDto();
        JsonNode node = jp.getCodec().readTree(jp);

        reportDto.setUuid(UUID.fromString(node.get("uuid").asText()));
        reportDto.setDtCreate(LocalDateTime
                .ofInstant(Instant.ofEpochMilli(node.get("dt_create").asLong()), ZoneOffset.UTC));
        reportDto.setDtUpdate(LocalDateTime
                .ofInstant(Instant.ofEpochMilli(node.get("dt_update").asLong()), ZoneOffset.UTC));
        reportDto.setDescription(node.get("description").asText());
        reportDto.setType(ReportType.valueOf(node.get("report_type").asText()));
        reportDto.setStatus(Status.valueOf(node.get("status").asText()));

        ReportDto reportDto1 = new ObjectMapper().readValue(jp, ReportDto.class);
        return reportDto1;
    }
}
