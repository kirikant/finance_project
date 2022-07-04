package com.telegram.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.telegram.dto.CategoryDto;

import java.io.IOException;
import java.time.ZoneId;

public class CategoryDtoSerializer extends StdSerializer<CategoryDto> {

    public CategoryDtoSerializer() {
        this(null);
    }

    public CategoryDtoSerializer(Class<CategoryDto> t) {
        super(t);
    }

    @Override
    public void serialize(
            CategoryDto value, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        gen.writeStartObject();
        gen.writeStringField("uuid", value.getUuid().toString());
        gen.writeNumberField("dt_create", value.getDtCreate()
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        gen.writeNumberField("dt_update", value.getDtUpdate()
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        gen.writeStringField("title", value.getTitle());
        gen.writeEndObject();
    }
}
