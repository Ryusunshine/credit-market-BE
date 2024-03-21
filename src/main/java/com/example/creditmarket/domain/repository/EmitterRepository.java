package com.example.creditmarket.domain.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EmitterRepository { // 로컬캐시로 emitter 저장

    private Map<String, SseEmitter> emitterMap = new HashMap<>();

    public SseEmitter save(Long userId, SseEmitter emitter) {
        final String key = getKey(userId);
        log.info("Set Emitter to Redis {}({})", key, emitter);
        emitterMap.put(key, emitter);
        return emitter;
    }

    public void delete(Long userId) {
        emitterMap.remove(getKey(userId));
    }

    public Optional<SseEmitter> get(Long userId) { //emitter가 null일수도 잇으니깐 optional 처리
        SseEmitter result = emitterMap.get(getKey(userId));
        log.info("Get Emitter from Redis {}", result);
        return Optional.ofNullable(result);
    }

    private String getKey(Long userId) {
        return "emitter:UID:" + userId;
    }
}

