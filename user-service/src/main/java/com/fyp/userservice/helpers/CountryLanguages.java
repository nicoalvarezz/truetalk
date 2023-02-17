package com.fyp.userservice.helpers;

import java.util.HashMap;

public class CountryLanguages {
    private HashMap<String, String> countries = new HashMap<>();

    public CountryLanguages() {
        countries.put("Canada", "English, French");
        countries.put("Mexico", "Spanish");
        countries.put("France", "French");
        countries.put("Spain", "Spanish");
        countries.put("Japan", "Japanese");
        countries.put("Brazil", "Portuguese");
        countries.put("China", "Mandarin");
        countries.put("India", "Hindi, English");
        countries.put("Russia", "Russian");
        countries.put("Australia", "English");
        countries.put("Germany", "German");
        countries.put("Italy", "Italian");
        countries.put("Argentina", "Spanish");
        countries.put("South Africa", "Afrikaans, English");
        countries.put("United Kingdom", "English");
        countries.put("Nigeria", "English");
        countries.put("Iran", "Persian");
        countries.put("Saudi Arabia", "Arabic");
        countries.put("Egypt", "Arabic");
        countries.put("South Korea", "Korean");
        countries.put("Ireland", "English");
    }

    public HashMap<String, String> getCountries() {
        return countries;
    }

    public String getLanguage(String country) {
        return countries.get(country);
    }
}