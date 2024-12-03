package org.example.smartresidenceactivity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.reqest.ResidenceRequest;
import org.example.smartresidenceactivity.model.response.ResidenceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/residence")
@Api(value = "Residence Controller", tags = {"Residence API"})
public class ResidenceController {
    @ApiOperation(value = "Создание жилого комплекса")
    @PostMapping
    public ResponseEntity<ResidenceResponse> createResidence(@RequestBody ResidenceRequest residence) {
        return null;
    }

    @ApiOperation(value = "Получение жилого комплекса по id")
    @GetMapping("/{id}")
    public ResponseEntity<ResidenceResponse> getResidenceById(@PathVariable UUID id) {
        return null;
    }

    @ApiOperation(value = "Получение всех жилых комплексов")
    @GetMapping
    public ResponseEntity<List<ResidenceResponse>> getAllResidences() {
        return null;
    }

    @ApiOperation(value = "Обновление жилого комплекса")
    @PutMapping("/{id}")
    public ResponseEntity<ResidenceResponse> updateResidence(@PathVariable UUID id, @RequestBody ResidenceRequest residence) {
        return null;
    }

    @ApiOperation(value = "Удаление жилого комплекса по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResidence(@PathVariable UUID id) {
        return null;
    }
}
