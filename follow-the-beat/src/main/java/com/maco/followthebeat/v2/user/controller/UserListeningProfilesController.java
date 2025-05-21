package com.maco.followthebeat.v2.user.controller;

import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.user.dto.UserEmbeddingPayloadDto;
import com.maco.followthebeat.v2.user.dto.UserListeningProfileDto;
import com.maco.followthebeat.v2.user.service.impl.UserListeningProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/listening-profiles")
@RequiredArgsConstructor
public class UserListeningProfilesController {

    private UserListeningProfileServiceImpl service;

    @GetMapping
    public List<UserListeningProfileDto> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserListeningProfileDto one(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public UserListeningProfileDto create(@RequestBody UserListeningProfileDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public UserListeningProfileDto update(@PathVariable UUID id, @RequestBody UserListeningProfileDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

}
