package com.maco.followthebeat.v2.core.controller;

import com.maco.followthebeat.v2.core.entity.Festival;
import com.maco.followthebeat.v2.core.dto.FestivalDTO;
import com.maco.followthebeat.v2.core.mappers.FestivalMapper;
import com.maco.followthebeat.v2.core.service.interfaces.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/festivals")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;
    private final FestivalMapper festivalMapper;

    @GetMapping
    public ResponseEntity<List<FestivalDTO>> getAllFestivals() {
        List<Festival> festivals = festivalService.getAll();
        List<FestivalDTO> dtos = festivals.stream()
                .map(festivalMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}