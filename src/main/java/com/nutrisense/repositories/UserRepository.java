package com.nutrisense.repositories;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.nutrisense.models.user.User;

public class UserRepository {

    private static final String FILE_PATH = "src/main/resources/data/users.json";
    private List<User> users;

    public UserRepository() {
        Type listType = new TypeToken<List<User>>(){}.getType();
        this.users = DatabaseJSON.loadList(FILE_PATH, listType);
    }

    public List<User> findAll() {
        return users;
    }

    public User findById(String id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void save(User user) {
        users.add(user);
        DatabaseJSON.saveList(FILE_PATH, users);
    }
}
