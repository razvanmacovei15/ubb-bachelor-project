package com.maco.followthebeat.v2.spotify.auth.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.maco.followthebeat.v2.spotify.auth.service.interfaces.StateCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StateCacheServiceImpl implements StateCacheService {
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
