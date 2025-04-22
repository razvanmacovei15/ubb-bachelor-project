package com.maco.followthebeat.service;

import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.repo.UserRepo;
import com.maco.followthebeat.service.interfaces.UserServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceI {
    @Override
    public UUID createUser() {
        return null;
    }

    @Override
    public UUID createAnonymousUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setAnonymous(true);
        userRepo.save(user);
        return user.getId();
    }

    @Override
    public void deleteUser(UUID userId) {

    }

    @Override
    public void updateUser(UUID userId) {

    }

    @Override
    public String getUser(UUID userId) {
        return "";
    }

    private final UserRepo userRepo;


}
