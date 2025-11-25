package com.nutrisense.models;

public class GiziResult {
    private String foodName;
    private double calories, protein, fat, carbs;
    public GiziResult(String n,double c,double p,double f,double car){ this.foodName=n; calories=c; protein=p; fat=f; carbs=car; }
    public String getFoodName(){return foodName;} public double getCalories(){return calories;} public double getProtein(){return protein;} public double getFat(){return fat;} public double getCarbs(){return carbs;}
    public String detailedString(){
        return String.format("%s\nKalori: %.1f kCal\nProtein: %.1fg\nLemak: %.1fg\nKarbo: %.1fg", foodName, calories, protein, fat, carbs);
    }
}
