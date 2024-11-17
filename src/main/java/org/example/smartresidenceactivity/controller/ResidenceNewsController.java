package org.example.smartresidenceactivity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.reqest.ResidenceNewsRequest;
import org.example.smartresidenceactivity.model.response.ResidenceNewsResponse;
import org.example.smartresidenceactivity.service.ResidenceNewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/residence-news")
@Api(value = "ResidenceNews Controller", tags = {"ResidenceNews API"})
public class ResidenceNewsController {
    private final ResidenceNewsService residenceNewsService;

    @ApiOperation(value = "Создание новости")
    @PostMapping
    public ResponseEntity<ResidenceNewsResponse> createResidenceNews(@RequestBody ResidenceNewsRequest residenceNews) {
        return ResponseEntity.ok(residenceNewsService.createResidenceNews(residenceNews));
    }

    @ApiOperation(value = "Получение новости по id")
    @GetMapping("/{id}")
    public ResponseEntity<ResidenceNewsResponse> getResidenceNewsById(@PathVariable UUID id) {
        return ResponseEntity.ok(residenceNewsService.getResidenceNewsById(id));
    }

    @ApiOperation(value = "Получение всех новостей")
    @GetMapping
    public ResponseEntity<List<ResidenceNewsResponse>> getAllResidenceNews(@PathVariable UUID residenceId) {
        return ResponseEntity.ok(residenceNewsService.getAllResidenceNews(residenceId));
    }

    @ApiOperation(value = "Обновление новости")
    @PutMapping("/{id}")
    public ResponseEntity<ResidenceNewsResponse> updateResidenceNews(@PathVariable UUID id, @RequestBody ResidenceNewsRequest residenceNews) {
        return ResponseEntity.ok(residenceNewsService.updateResidenceNews(id, residenceNews));
    }

    @ApiOperation(value = "Удаление новости по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResidenceNews(@PathVariable UUID id) {
        residenceNewsService.deleteResidenceNews(id);
        return ResponseEntity.noContent().build();
    }
}
