package com.sparta.sprintbackofficeproject.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.sprintbackofficeproject.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisUtil {
    private final StringRedisTemplate template;


    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    public void saveSignupRequestDto(String key, SignupRequestDto requestDto, long duration) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String value = mapper.writeValueAsString(requestDto);
            Duration expireDuration = Duration.ofSeconds(duration);
            String prefixedKey = "signup:" + key;
            template.opsForValue().set(prefixedKey, value, expireDuration);
        } catch (Exception e) {
        }
    }

    public <T> Optional<T> getClass(String key, Class<T> classType) {
        String prefixedKey = "signup:" + key;
        String value = template.opsForValue().get(prefixedKey);
        if (value == null) {
            return Optional.empty();
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return Optional.of(mapper.readValue(value, classType));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}