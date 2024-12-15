package org.example.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.mainservice.model.reqest.ResidenceRequest;
import org.example.mainservice.model.response.ResidenceResponse;
import org.example.mainservice.service.ResidenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/residence")
public class ResidenceController {
    private final ResidenceService residenceService;
    @PostMapping
    public ResponseEntity<ResidenceResponse> createResidence(@RequestBody ResidenceRequest residence) {
        return ResponseEntity.ok(residenceService.createResidence(residence));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResidenceResponse> getResidenceById(@PathVariable UUID id) {
        return ResponseEntity.ok(residenceService.getResidenceById(id));
    }
    @GetMapping
    public ResponseEntity<List<ResidenceResponse>> getAllResidences() {
        return ResponseEntity.ok(residenceService.getAllResidences());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResidenceResponse> updateResidence(@PathVariable UUID id, @RequestBody ResidenceRequest residence) {
        return ResponseEntity.ok(residenceService.updateResidence(id, residence));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResidence(@PathVariable UUID id) {
        residenceService.deleteResidence(id);
        return ResponseEntity.noContent().build();
    }
}
