package com.maco.followthebeat.v2.faker;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fake-data")
@RequiredArgsConstructor
public class FakeDataController {
    private final FakeAdapter fakeAdapter;
    @PostMapping("/festivals")
    public ResponseEntity<String> generateFakeFestivals(@RequestParam(defaultValue = "1000") int count){
        fakeAdapter.generateAndSaveFakeFestivals(count);
        return ResponseEntity.ok("Successfully generated " + count + " fake festivals.");
    }
}
