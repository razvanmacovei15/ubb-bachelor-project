package com.maco.followthebeat.v2.user.controller;

import com.maco.followthebeat.v2.core.service.interfaces.FestivalUserService;
import com.maco.followthebeat.v2.user.context.IsConnected;
import com.maco.followthebeat.v2.user.context.UserContext;
import com.maco.followthebeat.v2.user.dto.UpdateUserRequest;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserContext userContext;
    private final FestivalUserService festivalUserService;

    @IsConnected
    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        User currentUser = userContext.getOrThrow();
        userService.updateUser(request, currentUser);
        return ResponseEntity.ok("User updated successfully.");
    }

    @IsConnected
    @GetMapping("/hasFestival")
    public Boolean hasFestivalUserGenerated(@RequestParam String festivalId) {
        User user = userContext.getOrThrow();
        return festivalUserService.existsByFestivalIdAndUserId(UUID.fromString(festivalId), user.getId());
    }
}
