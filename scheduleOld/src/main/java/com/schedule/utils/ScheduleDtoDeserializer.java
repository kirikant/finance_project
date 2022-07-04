package com.schedule.utils;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.schedule.dto.ScheduleDto;
import com.schedule.entity.TimeUnit;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

public class ScheduleDtoDeserializer extends StdDeserializer<ScheduleDto> {

    public ScheduleDtoDeserializer() {
        this(null);
    }

    public ScheduleDtoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ScheduleDto deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException {
        ScheduleDto scheduleDto = new ScheduleDto();
        JsonNode node = jp.getCodec().readTree(jp);
        scheduleDto.setStartTime(LocalDateTime
                .ofInstant(Instant.ofEpochMilli(node.get("start_time").asLong()), ZoneOffset.UTC));
        scheduleDto.setStopTime(LocalDateTime
                .ofInstant(Instant.ofEpochMilli(node.get("stop_time").asLong()), ZoneOffset.UTC));
        scheduleDto.setInterval(node.get("interval").asLong());
        scheduleDto.setTimeUnit(TimeUnit.valueOf(node.get("time_unit").asText()));

        return scheduleDto;
    }
}