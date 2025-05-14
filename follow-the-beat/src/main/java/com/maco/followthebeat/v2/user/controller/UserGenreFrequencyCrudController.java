package com.maco.followthebeat.v2.user.controller;

import com.maco.followthebeat.v2.user.dto.UserGenreFrequencyDto;
import com.maco.followthebeat.v2.user.service.interfaces.UserGenreFrequencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/genre-frequencies")
@RequiredArgsConstructor
public class UserGenreFrequencyCrudController {
    private final UserGenreFrequencyService service;

    @GetMapping
    public List<UserGenreFrequencyDto> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserGenreFrequencyDto one(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public UserGenreFrequencyDto create(@RequestBody UserGenreFrequencyDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public UserGenreFrequencyDto update(@PathVariable UUID id, @RequestBody UserGenreFrequencyDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}