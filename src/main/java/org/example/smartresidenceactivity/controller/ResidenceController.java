package org.example.smartresidenceactivity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.reqest.ResidenceRequest;
import org.example.smartresidenceactivity.model.response.ResidenceResponse;
import org.example.smartresidenceactivity.service.ResidenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/residence")
@Api(value = "Residence Controller", tags = {"Residence API"})
public class ResidenceController {
    private final ResidenceService residenceService;

    @ApiOperation(value = "Создание жилого комплекса")
    @PostMapping
    public ResponseEntity<ResidenceResponse> createResidence(@RequestBody ResidenceRequest residence) {
        return ResponseEntity.ok(residenceService.createResidence(residence));
    }

    @ApiOperation(value = "Получение жилого комплекса по id")
    @GetMapping("/{id}")
    public ResponseEntity<ResidenceResponse> getResidenceById(@PathVariable UUID id) {
        return ResponseEntity.ok(residenceService.getResidenceById(id));
    }

    @ApiOperation(value = "Получение всех жилых комплексов")
    @GetMapping
    public ResponseEntity<List<ResidenceResponse>> getAllResidences() {
        return ResponseEntity.ok(residenceService.getAllResidences());
    }

    @ApiOperation(value = "Обновление жилого комплекса")
    @PutMapping("/{id}")
    public ResponseEntity<ResidenceResponse> updateResidence(@PathVariable UUID id, @RequestBody ResidenceRequest residence) {
        return ResponseEntity.ok(residenceService.updateResidence(id, residence));
    }

    @ApiOperation(value = "Удаление жилого комплекса по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResidence(@PathVariable UUID id) {
        residenceService.deleteResidence(id);
        return ResponseEntity.noContent().build();
    }
}
