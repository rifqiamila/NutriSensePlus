package com.nutrisense.services;

import java.util.HashMap;
import java.util.Map;

public class FakeAuthService {
    private static FakeAuthService instance = new FakeAuthService();
    private Map<String,String> users = new HashMap<>();

    private FakeAuthService(){
        users.put("user","1234");      
        users.put("test_student","test");
        users.put("test_kitchen","test");
    }

    public static FakeAuthService getInstance(){ return instance; }
    public boolean loginStudent(String nisn, String pw){ return users.containsKey(nisn) && users.get(nisn).equals(pw); }
    public boolean loginKitchen(String user, String pw){ return users.containsKey(user) && users.get(user).equals(pw); }
    public boolean register(String u, String pw, String role){ users.put(u,pw); return true; }
}
