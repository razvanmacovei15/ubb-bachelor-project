package com.maco.followthebeat.v2.user.context;

import com.maco.followthebeat.v2.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserContext {
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public void set(User user) {
        currentUser.set(user);
    }

    public User getOrThrow() {
        User user = currentUser.get();
        if (user == null) {
            throw new IllegalStateException("User context is not set");
        }
        return user;
    }

    public void clear() {
        currentUser.remove();
    }
}
