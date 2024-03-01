package com.example.streetfoodfinder.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RedisServiceTest {
    private RedisService redisService;

    @BeforeEach
    public void setUp() {
        // 테스트 전에 RedisService 인스턴스 생성
        RedisConnectionFactory factory = new LettuceConnectionFactory("localhost", 6379);
        redisService = new RedisService(factory);
    }

    @AfterEach
    public void tearDown() {
        // 테스트 후에 Redis 연결 종료
        redisService = null;
    }

    @Test
    public void testSaveAndGetData() {
        // 테스트 데이터 저장
        String key = "testKey";
        String value = "testValue";
        redisService.saveData(key, value);

        // 저장된 데이터 조회
        String retrievedValue = redisService.getData(key);

        // 저장된 데이터가 올바르게 조회되었는지 확인
        assertEquals(value, retrievedValue);
    }
}
