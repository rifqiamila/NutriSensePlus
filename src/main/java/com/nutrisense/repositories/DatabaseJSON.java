package com.nutrisense.repositories;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;

/**
 * Helper untuk membaca & menulis list object ke JSON file.
 */
public class DatabaseJSON {

    private static final Gson gson = new Gson();

    public static <T> List<T> loadList(String path, Type type) {
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }
    }

    public static <T> void saveList(String path, List<T> data) {
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(data, writer);
        } catch (Exception e) {
            System.err.println("Gagal menyimpan data ke file: " + path);
        }
    }
}
