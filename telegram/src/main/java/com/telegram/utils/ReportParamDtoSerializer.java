package com.telegram.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.telegram.dto.ReportParamDto;

import java.io.IOException;
import java.time.ZoneOffset;
import java.util.UUID;

public class ReportParamDtoSerializer extends StdSerializer<ReportParamDto> {

    public ReportParamDtoSerializer() {
        this(null);
    }

    public ReportParamDtoSerializer(Class<ReportParamDto> t) {
        super(t);
    }

    @Override
    public void serialize(
            ReportParamDto value, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        gen.writeStartObject();
        gen.writeArrayFieldStart("accounts");
        for (UUID uuid:value.getAccounts()) {
            gen.writeString(uuid.toString());
        }
        gen.writeEndArray();
        gen.writeNumberField("from", value.getFromTime()
                .atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
        gen.writeNumberField("to", value.getToTime()
                .atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
        gen.writeArrayFieldStart("categories");
        for (UUID uuid:value.getCategories()) {
            gen.writeString(uuid.toString());
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
