package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class Managers {
    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8078/", getGson(), getDefaultHistory());
    }

    public static FileBackedTasksManager getFileBackedTasksManager(String fileName) {
        fileName = "resources/" + fileName;
        return FileBackedTasksManager.loadFromFile(new File(fileName));
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Instant.class, new InstantAdapter());
        return  gsonBuilder.create();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    private static class InstantAdapter extends TypeAdapter<Instant> {
        @Override
        public void write(JsonWriter jsonWriter, Instant instant) throws IOException {
            if (instant == null) {
                jsonWriter.value("null");
                return;
            }
            jsonWriter.value(instant.toString());
        }

        @Override
        public Instant read(JsonReader jsonReader) throws IOException {
            final String text = jsonReader.nextString();
            if (text.equals("null")) {
                return null;
            }
            return Instant.parse(text);
        }
    }
}
