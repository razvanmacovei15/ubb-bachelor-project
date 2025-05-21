package com.maco.followthebeat.v2.cache;

import java.util.UUID;

public interface RedisStateCacheService {
    void store(String state, UUID userId);
    UUID retrieve(String state);
    void invalidate(String state);
}
