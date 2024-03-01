package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisController {
    @Autowired
    private RedisService redisService;

    public void saveData(String key, String value) {
        redisService.saveData(key, value);
    }

    public String getData(String key) {
        return redisService.getData(key);
    }

    public static void main(String[] args) {
        RedisController redisController = new RedisController();
        redisController.saveData("key", "value");
        String retrievedValue = redisController.getData("key");
        System.out.println("Retrieved Value: " + retrievedValue);
    }
}
