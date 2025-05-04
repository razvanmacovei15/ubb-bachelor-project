package com.maco.followthebeat.v2.cache;

import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
@Service
public class RedisStateCacheServiceImpl implements RedisStateCacheService{
    private final StringRedisTemplate redis;
    private static final Duration STATE_TTL = Duration.ofMinutes(10);
    private static final Duration SESSION_TTL = Duration.ofHours(1);
    @Autowired
    public RedisStateCacheServiceImpl(StringRedisTemplate redis) {
        this.redis = redis;
    }

    @Override
    public void store(String state, UUID userId) {
        redis.opsForValue().set("state:" + state, userId.toString(), STATE_TTL);
    }

    @Override
    public UUID retrieve(String state) {
        String userId = redis.opsForValue().get("state:" + state);
        return userId != null ? UUID.fromString(userId) : null;
    }

    @Override
    public void invalidate(String state) {
        redis.delete("state:" + state);
        redis.delete("sessionForState:" + state);
    }

    public void storeSessionToken(String state, String sessionToken) {
        redis.opsForValue().set("sessionForState:" + state, sessionToken, STATE_TTL);
    }

    public String retrieveSessionToken(String state) {
        return redis.opsForValue().get("sessionForState:" + state);
    }

    public void storeUserForSession(String sessionToken, UUID userId) {
        redis.opsForValue().set("session:" + sessionToken, userId.toString(), SESSION_TTL);
    }

    public UUID getUserBySession(String sessionToken) {
        String userId = redis.opsForValue().get("session:" + sessionToken);
        return userId != null ? UUID.fromString(userId) : null;
    }
}
