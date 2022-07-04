package mailScheduler.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import mailScheduler.dto.ScheduleDto;

import java.io.IOException;
import java.time.ZoneOffset;

public class ScheduleDtoSerializer extends StdSerializer<ScheduleDto>{

    public ScheduleDtoSerializer() {
        this(null);
    }

    protected ScheduleDtoSerializer(Class<ScheduleDto> t) {
        super(t);
    }

    @Override
    public void serialize(ScheduleDto value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("start_time", String
                .valueOf(value.getStartTime().toInstant(ZoneOffset.UTC).toEpochMilli()));
        gen.writeStringField("interval", String
                .valueOf(value.getInterval()));
        gen.writeStringField("stop_time", String
                .valueOf(value.getStopTime().toInstant(ZoneOffset.UTC).toEpochMilli()));
        gen.writeStringField("time_unit", (value.getTimeUnit().toString()));
        gen.writeEndObject();
    }
}
