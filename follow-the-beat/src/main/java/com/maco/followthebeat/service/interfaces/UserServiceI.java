package com.maco.followthebeat.service.interfaces;

import java.util.UUID;

public interface UserServiceI {
    UUID createUser();
    UUID createAnonymousUser();
    void deleteUser(UUID userId);
    void updateUser(UUID userId);
    String getUser(UUID userId);
}
