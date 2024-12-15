package org.example.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.mainservice.model.reqest.ResidenceNewsRequest;
import org.example.mainservice.model.response.ResidenceNewsResponse;
import org.example.mainservice.service.ResidenceNewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/residence-news")
public class ResidenceNewsController {
    private final ResidenceNewsService residenceNewsService;
    @PostMapping
    public ResponseEntity<ResidenceNewsResponse> createResidenceNews(@RequestBody ResidenceNewsRequest residenceNews) {
        return ResponseEntity.ok(residenceNewsService.createResidenceNews(residenceNews));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResidenceNewsResponse> getResidenceNewsById(@PathVariable UUID id) {
        return ResponseEntity.ok(residenceNewsService.getResidenceNewsById(id));
    }
    @GetMapping("/all/{residenceId}")
    public ResponseEntity<List<ResidenceNewsResponse>> getAllResidenceNews(@PathVariable UUID residenceId) {
        return ResponseEntity.ok(residenceNewsService.getAllResidenceNews(residenceId));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResidenceNewsResponse> updateResidenceNews(@PathVariable UUID id, @RequestBody ResidenceNewsRequest residenceNews) {
        return ResponseEntity.ok(residenceNewsService.updateResidenceNews(id, residenceNews));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResidenceNews(@PathVariable UUID id) {
        residenceNewsService.deleteResidenceNews(id);
        return ResponseEntity.noContent().build();
    }
}
