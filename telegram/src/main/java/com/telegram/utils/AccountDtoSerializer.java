package com.telegram.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.telegram.dto.AccountDto;

import java.io.IOException;
import java.time.ZoneOffset;

public class AccountDtoSerializer extends StdSerializer<AccountDto> {

    public AccountDtoSerializer() {
        this(null);
    }

    public AccountDtoSerializer(Class<AccountDto> t) {
        super(t);
    }

    @Override
    public void serialize(
            AccountDto value, JsonGenerator gen, SerializerProvider provider)
      throws IOException, JsonProcessingException {
 
        gen.writeStartObject();
        gen.writeStringField("uuid", value.getUuid().toString());
        gen.writeNumberField("dt_create", value.getDtCreate()
                .toInstant(ZoneOffset.UTC).toEpochMilli());
        gen.writeNumberField("dt_update", value.getDtUpdate()
                .toInstant(ZoneOffset.UTC).toEpochMilli());
        gen.writeStringField("title", value.getTitle());
        gen.writeStringField("description", value.getDescription());
        gen.writeNumberField("balance", value.getBalance().doubleValue());
        gen.writeStringField("type", value.getType().name());
        gen.writeStringField("currency", value.getCurrency().toString());
        gen.writeEndObject();
    }
}