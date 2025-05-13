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
        log.info("SessionTokenFilter: Processing request...");
        String authHeader = request.getHeader("Authorization");
        log.info("Authorization header: {}", authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            log.info("Extracting token from Authorization header...");
            String token = authHeader.substring(7);
            log.info("Extracted token: {}", token);
            UUID userId = stateCacheService.getUserBySession(token);
            log.info("User ID from token: {}", userId);

            if (userId != null) {
                userService.findUserById(userId).ifPresent(userContext::set);
                log.info("User context set for user ID: {}", userId);
            }
        }

        try {
            filterChain.doFilter(request, response);
            log.info("Filter chain processed successfully.");
        } finally {
            userContext.clear();
        }
    }
}
