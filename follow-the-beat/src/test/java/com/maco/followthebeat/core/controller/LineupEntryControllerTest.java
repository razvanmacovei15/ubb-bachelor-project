package com.maco.followthebeat.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maco.followthebeat.v2.cache.RedisStateCacheServiceImpl;
import com.maco.followthebeat.v2.core.controller.LineupEntryController;
import com.maco.followthebeat.v2.core.dto.LineupEntryDTO;
import com.maco.followthebeat.v2.core.entity.LineupEntry;
import com.maco.followthebeat.v2.core.service.interfaces.LineupEntryService;
import com.maco.followthebeat.v2.user.context.SessionTokenFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LineupEntryController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class LineupEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LineupEntryService lineupEntryService;

    @MockBean
    private RedisStateCacheServiceImpl redisStateCacheService;

    @MockBean
    private SessionTokenFilter sessionTokenFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddLineupEntry_Success() throws Exception {
        LineupEntryDTO dto = new LineupEntryDTO();
        dto.setUserId(UUID.randomUUID());
        dto.setConcertId(UUID.randomUUID());

        LineupEntry savedEntry = new LineupEntry();
        savedEntry.setId(UUID.randomUUID());

        when(lineupEntryService.createLineupEntry(any(LineupEntryDTO.class))).thenReturn(savedEntry);

        mockMvc.perform(post("/api/lineup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetLineupEntry_Found() throws Exception {
        UUID entryId = UUID.randomUUID();
        LineupEntry entry = new LineupEntry();
        entry.setId(entryId);

        when(lineupEntryService.getById(entryId)).thenReturn(Optional.of(entry));

        mockMvc.perform(get("/api/lineup/{id}", entryId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetLineupEntry_NotFound() throws Exception {
        UUID entryId = UUID.randomUUID();

        when(lineupEntryService.getById(entryId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/lineup/{id}", entryId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserLineup_Found() throws Exception {
        UUID userId = UUID.randomUUID();
        List<LineupEntry> lineup = List.of(new LineupEntry());

        when(lineupEntryService.getLineupForUserId(userId)).thenReturn(lineup);

        mockMvc.perform(get("/api/lineup/user/{userId}", userId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserLineup_NotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        when(lineupEntryService.getLineupForUserId(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/lineup/user/{userId}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateLineupEntry_Success() throws Exception {
        UUID entryId = UUID.randomUUID();
        LineupEntryDTO dto = new LineupEntryDTO();

        LineupEntry updatedEntry = new LineupEntry();
        updatedEntry.setId(entryId);

        when(lineupEntryService.updateLineupEntry(eq(entryId), any(LineupEntryDTO.class))).thenReturn(updatedEntry);

        mockMvc.perform(put("/api/lineup/{id}", entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteLineupEntry_Success() throws Exception {
        UUID entryId = UUID.randomUUID();
        LineupEntry entry = new LineupEntry();
        entry.setId(entryId);

        when(lineupEntryService.getById(entryId)).thenReturn(Optional.of(entry));
        Mockito.doNothing().when(lineupEntryService).delete(entryId);

        mockMvc.perform(delete("/api/lineup/{id}", entryId).with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteLineupEntry_NotFound() throws Exception {
        UUID entryId = UUID.randomUUID();

        when(lineupEntryService.getById(entryId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/lineup/{id}", entryId).with(csrf()))
                .andExpect(status().isNotFound());
    }
}
