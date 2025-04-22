package com.maco.followthebeat.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StateCacheService {
    private final Cache<String, UUID> stateToUserCache;

    @Autowired
    public StateCacheService(Cache<String, UUID> stateToUserCache) {
        this.stateToUserCache = stateToUserCache;
    }

    public void store(String state, UUID userId){
        stateToUserCache.put(state, userId);
    }

    public UUID retrieve(String state){
        return stateToUserCache.getIfPresent(state);
    }

    public void invalidate(String state){
        stateToUserCache.invalidate(state);
    }
}
