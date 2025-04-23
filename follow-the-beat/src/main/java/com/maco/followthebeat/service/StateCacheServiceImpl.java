package com.maco.followthebeat.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StateCacheServiceImpl implements com.maco.followthebeat.service.interfaces.StateCacheService {
    private final Cache<String, UUID> stateToUserCache;

    @Autowired
    public StateCacheServiceImpl(Cache<String, UUID> stateToUserCache) {
        this.stateToUserCache = stateToUserCache;
    }
    @Override
    public void store(String state, UUID userId){
        stateToUserCache.put(state, userId);
    }
    @Override
    public UUID retrieve(String state){
        return stateToUserCache.getIfPresent(state);
    }
    @Override
    public void invalidate(String state){
        stateToUserCache.invalidate(state);
    }
}
