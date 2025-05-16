package com.maco.followthebeat.v2.user.context;

import com.maco.followthebeat.v2.cache.RedisStateCacheServiceImpl;
import com.maco.followthebeat.v2.spotify.auth.service.interfaces.StateCacheService;
import com.maco.followthebeat.v2.user.context.UserContext;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
@Slf4j
public class SessionTokenFilter extends OncePerRequestFilter {

    private final RedisStateCacheServiceImpl stateCacheService;
    private final UserService userService;
    private final UserContext userContext;

    public SessionTokenFilter(RedisStateCacheServiceImpl stateCacheService, UserService userService, UserContext userContext) {
        this.stateCacheService = stateCacheService;
        this.userService = userService;
        this.userContext = userContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            UUID userId = stateCacheService.getUserBySession(token);

            if (userId != null) {
                userService.findUserById(userId).ifPresent(userContext::set);
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            userContext.clear();
        }
    }
}
