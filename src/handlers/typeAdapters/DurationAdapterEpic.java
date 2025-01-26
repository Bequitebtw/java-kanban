package handlers.typeAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapterEpic extends TypeAdapter<Duration> {
    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(duration.toHours());
        }
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        if (jsonReader.nextString() == null) {
            return null;
        } else {
            return Duration.ofHours(jsonReader.nextLong());
        }
    }
}