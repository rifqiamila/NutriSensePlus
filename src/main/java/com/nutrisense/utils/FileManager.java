package com.nutrisense.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // ==========================
    // READ JSON FILE AS STRING
    // ==========================
    public static String readFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
    }

    // ==========================
    // WRITE STRING TO FILE
    // ==========================
    public static void writeFile(String filePath, String content) throws IOException {
        Files.writeString(Path.of(filePath), content, StandardCharsets.UTF_8);
    }

    // ==========================
    // READ JSON → OBJECT
    // ==========================
    public static <T> T readJson(String filePath, Class<T> clazz) throws IOException {
        String json = readFile(filePath);
        return gson.fromJson(json, clazz);
    }

    // ==========================
    // READ JSON → ARRAY/LIST
    // ==========================
    public static <T> T readJson(String filePath, java.lang.reflect.Type type) throws IOException {
        String json = readFile(filePath);
        return gson.fromJson(json, type);
    }

    // ==========================
    // WRITE OBJECT → JSON FILE
    // ==========================
    public static void writeJson(String filePath, Object data) throws IOException {
        String json = gson.toJson(data);
        writeFile(filePath, json);
    }

    // ==========================
    // LOAD RESOURCE (JAR SAFE)
    // ==========================
    public static InputStream loadResource(String resourcePath) {
        return FileManager.class.getClassLoader().getResourceAsStream(resourcePath);
    }
}
