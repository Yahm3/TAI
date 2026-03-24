package com.chat;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class ChatStorage {
  private static final String FILE_PATH = "chat_history.json";
  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public static void saveHistory(List<JsonObject> history) {
    try (Writer writer = new FileWriter(FILE_PATH)) {
      gson.toJson(history, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<JsonObject> loadHistory() {
    File file = new File(FILE_PATH);
    if (!file.exists())
      return new ArrayList<>();

    try (Reader reader = new FileReader(file)) {
      return gson.fromJson(reader, new TypeToken<List<JsonObject>>() {
      }.getType());
    } catch (IOException e) {
      return new ArrayList<>();
    }
  }
}
