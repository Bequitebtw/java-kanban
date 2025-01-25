package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import manager.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//class LocalDateTimeAdapterEpic extends TypeAdapter<LocalDateTime> {
//    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//
//    @Override
//    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
//        if (localDateTime == null) {
//            jsonWriter.nullValue();
//            return;
//        }
//        jsonWriter.value(localDateTime.format(dtf));
//    }
//
//    @Override
//    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
//        if (jsonReader.nextString().equals("null")) {
//            return LocalDateTime.now();
//        }
//        return LocalDateTime.parse(jsonReader.nextString(), dtf);
//    }
//}
//
//class DurationAdapterEpic extends TypeAdapter<Duration> {
//    @Override
//    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
//        if (duration == null) {
//            jsonWriter.nullValue();
//            return;
//        }
//        jsonWriter.value(duration.toHours());
//    }
//
//    @Override
//    public Duration read(final JsonReader jsonReader) throws IOException {
//        if (jsonReader.nextString().equals("null")) {
//            return Duration.ofHours(0);
//        }
//        return Duration.ofHours(jsonReader.nextLong());
//    }
//}


class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null){
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(localDateTime.format(dtf));
        }
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        if(jsonReader.nextString() == null){
            return null;
        }
        else {
            return LocalDateTime.parse(jsonReader.nextString(), dtf);
        }
    }
}

class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        if(duration == null){
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(duration.toHours());
        }
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        if(jsonReader.nextString() == null){
            return null;
        }
        else{
            return Duration.ofHours(jsonReader.nextLong());
        }
    }
}

public class BaseHttpHandler {
//    Gson epicGson = new GsonBuilder()
//            .setPrettyPrinting()
//            .registerTypeAdapter(LocalDateTimeAdapterEpic.class,new LocalDateTimeAdapterEpic())
//            .registerTypeAdapter(DurationAdapterEpic.class,new DurationAdapterEpic())
//            .create();
    Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .serializeNulls()
            .create();
    protected final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected final InMemoryTaskManager inMemoryTaskManager;

    public BaseHttpHandler() {
        inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.createTask(new Task("TASK1", "DEKSTASK1",
                LocalDateTime.of(2024, 1, 1, 1, 1), Duration.ofHours(20)));//1
//        inMemoryTaskManager.createTask(new Task("TASK2", "DEKSTASK2",
//                LocalDateTime.of(2024, 1, 1, 1, 1), Duration.ofHours(20)));//2
//        inMemoryTaskManager.createTask(new Task("TASK3", "DEKSTASK3",
//                LocalDateTime.of(2024, 1, 1, 1, 1), Duration.ofHours(20)));//3
//        inMemoryTaskManager.createEpic(new Epic("EPIC1", "DESKEPIC1"));//4
//        inMemoryTaskManager.createEpic(new Epic("EPIC2", "DESKEPIC2"));//5
        inMemoryTaskManager.createEpic(new Epic("EPIC3", "DESKEPIC3"));//6
        inMemoryTaskManager.createSubtask(new Subtask("SUBTASK1", "DEKSSUBTASK1",
                LocalDateTime.of(2024, 1, 1, 1, 1), Duration.ofHours(20)), 2);//7
//        inMemoryTaskManager.createSubtask(new Subtask("SUBTASK1", "DEKSSUBTASK1",
//                LocalDateTime.of(2024, 1, 1, 1, 1), Duration.ofHours(20)), 4);//8
    }

    protected void sendCode200(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendCode201(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(201, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendCode404(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendCode406(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
}