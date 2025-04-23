package com.maco.followthebeat.service.interfaces;

import java.util.UUID;

public interface StateCacheService {

    void store(String state, UUID userId);
    UUID retrieve(String state);
    void invalidate(String state);

}
