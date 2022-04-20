package me.kafein.elitegenerator.util.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.lang.reflect.Type;

public class JsonObject {

    final private JsonParser jsonParser = new JsonParser();

    final private Gson gson;
    private com.google.gson.JsonObject jsonObject;

    public JsonObject(final Gson gson, final String json) {
        this.gson = gson;
        this.jsonObject = jsonParser.parse(json).getAsJsonObject();
    }

    public JsonObject(final Gson gson, final File file) {
        this.gson = gson;
        FileReader fileReader;
        try {
            fileReader = new FileReader(file);
            jsonObject = gson.fromJson(fileReader, com.google.gson.JsonObject.class);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonObject(final Gson gson) {
        this.gson = gson;
        this.jsonObject = new com.google.gson.JsonObject();
    }

    public void add(final String property, final String value) {
        jsonObject.addProperty(property, value);
    }

    public void add(final String property, final Number value) {
        jsonObject.addProperty(property, value);
    }

    public void add(final String property, final boolean value) {
        jsonObject.addProperty(property, value);
    }

    public void add(final String arrayProperty, final String property, final String[] array) {
        final JsonArray jsonArray = new JsonArray();
        for (String value : array) {
            final com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
            jsonObject.addProperty(property, value);
            jsonArray.add(jsonObject);
        }
        this.jsonObject.add(arrayProperty, jsonArray);
    }

    public <T> void addObject(final String property, final T object) {
        jsonObject.add(property, gson.toJsonTree(object));
    }

    public void addJsonTree(final String property, final JsonElement jsonElement) {
        jsonObject.add(property, jsonElement);
    }

    public boolean has(final String property) {
        return jsonObject.has(property);
    }

    public String getString(final String property) {
        return gson.fromJson(jsonObject.get(property), String.class);
    }

    public Number getNumber(final String property) {
        return gson.fromJson(jsonObject.get(property), Number.class);
    }

    public boolean getBoolean(final String property) {
        return gson.fromJson(jsonObject.get(property), boolean.class);
    }

    public String[] getArray(final String property) {
        final JsonArray jsonArray = jsonObject.get(property).getAsJsonArray();
        final String[] array = new String[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) array[i] = jsonArray.get(i).toString();
        return array;
    }

    public <T> T getObject(final String property, final Type type) {
        return (T) gson.fromJson(jsonObject.get(property), type);
    }

    public JsonObject getIJsonObject(final String property) {
        return new JsonObject(gson, jsonObject.get(property).toString());
    }

    public com.google.gson.JsonObject parse() {
        return jsonObject;
    }

    public com.google.gson.JsonObject parseToJsonObject(final String json) {
        return jsonParser.parse(json).getAsJsonObject();
    }

    public JsonObject parseToIJsonObject(final String json) {
        return new JsonObject(gson, json);
    }

    public JsonArray parseToArray(final String json) {
        return jsonParser.parse(json).getAsJsonArray();
    }

    public void saveToFile(final File file) {
        try {
            final FileWriter fileWriter = new FileWriter(file);
            gson.toJson(parse(), fileWriter);
            fileWriter.flush();
            fileWriter.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
