package com.nutrisense.services;

import com.nutrisense.models.GiziResult;
import java.util.Random;

public class FakeGiziService {
    private static FakeGiziService instance = new FakeGiziService();
    private Random r = new Random();
    public static FakeGiziService getInstance(){ return instance; }
    public GiziResult analyzeFood(String name, double grams){
        double base = 80 + r.nextDouble()*220;
        double cal = base * grams/100.0;
        double prot = (5 + r.nextDouble()*30) * grams/100.0;
        double fat = (2 + r.nextDouble()*15) * grams/100.0;
        double carbs = (10 + r.nextDouble()*60) * grams/100.0;
        return new GiziResult(name.isEmpty()? "Unknown":name, cal, prot, fat, carbs);
    }
}
