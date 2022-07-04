package com.reports.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.reports.dto.ReportDto;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class ReportDtoSerializer extends StdSerializer<ReportDto> {

    public ReportDtoSerializer() {
        this(null);
    }

    public ReportDtoSerializer(Class<ReportDto> t) {
        super(t);
    }

    @Override
    public void serialize(
            ReportDto value, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        gen.writeStartObject();
        gen.writeStringField("uuid", value.getUuid().toString());
        gen.writeNumberField("dt_create", value.getDtCreate()
                .atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
        gen.writeNumberField("dt_update", value.getDtUpdate()
                .atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
        gen.writeStringField("status", value.getStatus().toString());
        gen.writeStringField("report_type", value.getReportType().toString());
        gen.writeStringField("description", value.getDescription());
        gen.writeStringField("type", value.getReportType().name());
        gen.writeFieldName("report_param");
        gen.writeObject(value.getReportParamDto());

        gen.writeEndObject();
    }
}
