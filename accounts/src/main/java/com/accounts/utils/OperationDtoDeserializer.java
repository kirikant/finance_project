package com.accounts.utils;


import com.accounts.dto.OperationDto;
import com.accounts.entity.Type;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

public class OperationDtoDeserializer extends StdDeserializer<OperationDto> {

    public OperationDtoDeserializer() {
        this(null);
    }

    public OperationDtoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public OperationDto deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
        OperationDto operationDto = new OperationDto();
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.get("date")!=null) operationDto.setDateOperation(LocalDateTime
                .ofInstant(Instant.ofEpochMilli(node.get("date").asLong()), ZoneOffset.UTC));
        operationDto.setDescription(node.get("description").asText());
        operationDto.setCategory(UUID.fromString(node.get("category").asText()));
        operationDto.setValue(BigDecimal.valueOf(node.get("value").asLong()));
        operationDto.setCurrency(UUID.fromString(node.get("currency").asText()));

        return operationDto;
    }
}