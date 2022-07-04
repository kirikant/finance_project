package com.telegram.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.telegram.dto.AccountDto;
import com.telegram.entity.Type;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class AccountDtoDeserializer extends StdDeserializer<AccountDto> {

    public AccountDtoDeserializer() {
        this(null);
    }

    public AccountDtoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public AccountDto deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        AccountDto accountDto = new AccountDto();
        JsonNode node = jp.getCodec().readTree(jp);

        accountDto.setUuid(UUID.fromString(node.get("uuid").asText()));
        accountDto.setDtCreate(LocalDateTime
                .ofInstant(Instant.ofEpochMilli(node.get("dt_create").asLong()), ZoneOffset.UTC));
        accountDto.setDtUpdate(LocalDateTime
                .ofInstant(Instant.ofEpochMilli(node.get("dt_update").asLong()), ZoneOffset.UTC));
        accountDto.setDescription(node.get("description").asText());
        accountDto.setBalance(BigDecimal.valueOf(node.get("balance").asDouble()));
        accountDto.setTitle(node.get("title").asText());
        accountDto.setType(Type.valueOf(node.get("type").asText()));
        accountDto.setCurrency(UUID.fromString(node.get("currency").asText()));

        return accountDto;
    }
}
