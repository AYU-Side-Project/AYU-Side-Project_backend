package com.example.streetfoodfinder.service;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisService {
    private ValueOperations<String, String> valueOps;

    public RedisService(RedisConnectionFactory factory) {
        LettuceConnectionFactory lettuceFactory = (LettuceConnectionFactory) factory;
        lettuceFactory.afterPropertiesSet();

        StringRedisTemplate template = new StringRedisTemplate(lettuceFactory);
        this.valueOps = template.opsForValue();
    }

    public void saveData(String key, String value) {
        valueOps.set(key, value);
        System.out.println("데이터를 저장했습니다.");
    }

    public String getData(String key) {
        String value = valueOps.get(key);
        System.out.println("저장된 데이터: " + value);
        return value;
    }
}
