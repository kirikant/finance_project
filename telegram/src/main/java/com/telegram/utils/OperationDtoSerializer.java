package com.telegram.utils;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.telegram.dto.OperationDto;

import java.io.IOException;
import java.time.ZoneOffset;

public class OperationDtoSerializer extends StdSerializer<OperationDto> {

    public OperationDtoSerializer() {
        this(null);
    }

    protected OperationDtoSerializer(Class<OperationDto> t) {
        super(t);
    }

    @Override
    public void serialize(OperationDto value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        gen.writeStartObject();
        gen.writeStringField("description", value.getDescription());
        gen.writeNumberField("value",value.getValue().doubleValue());
        gen.writeNumberField("date",value.getDateOperation()
                .toInstant(ZoneOffset.UTC).toEpochMilli());
        gen.writeStringField("category", value.getCategory().toString());
        gen.writeStringField("currency", value.getCurrency().toString());
        gen.writeEndObject();
    }
}
