package com.schedule.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.schedule.dto.ScheduledOperationDto;

import java.io.IOException;
import java.time.ZoneOffset;

public class ScheduledOperationDtoSerializer extends StdSerializer<ScheduledOperationDto> {

    public ScheduledOperationDtoSerializer() {
        this(null);
    }

    protected ScheduledOperationDtoSerializer(Class<ScheduledOperationDto> t) {
        super(t);
    }

    @Override
    public void serialize(ScheduledOperationDto value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("uuid", value.getUuid().toString());
        gen.writeNumberField("dt_create", value.getDtCreate()
                .toInstant(ZoneOffset.UTC).toEpochMilli());
        gen.writeNumberField("dt_update", value.getDtUpdate()
                .toInstant(ZoneOffset.UTC).toEpochMilli());

        gen.writeFieldName("operation");
        gen.writeObject(value.getOperationDto());
        gen.writeFieldName("schedule");
        gen.writeObject(value.getScheduleDto());
        gen.writeEndObject();
    }
}
