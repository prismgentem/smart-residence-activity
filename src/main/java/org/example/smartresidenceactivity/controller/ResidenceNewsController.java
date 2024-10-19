package org.example.smartresidenceactivity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.reqest.ResidenceNewsReqest;
import org.example.smartresidenceactivity.model.response.ResidenceNewsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/residence-news")
@Api(value = "ResidenceNews Controller", tags = {"ResidenceNews API"})
public class ResidenceNewsController {
    @ApiOperation(value = "Создание новости")
    @PostMapping
    public ResponseEntity<ResidenceNewsResponse> createResidenceNews(@RequestBody ResidenceNewsReqest residenceNews) {
        return null;
    }

    @ApiOperation(value = "Получение новости по id")
    @GetMapping("/{id}")
    public ResponseEntity<ResidenceNewsResponse> getResidenceNewsById(@PathVariable UUID id) {
        return null;
    }

    @ApiOperation(value = "Получение всех новостей")
    @GetMapping
    public ResponseEntity<List<ResidenceNewsResponse>> getAllResidenceNews() {
        return null;
    }

    @ApiOperation(value = "Обновление новости")
    @PutMapping("/{id}")
    public ResponseEntity<ResidenceNewsResponse> updateResidenceNews(@PathVariable UUID id, @RequestBody ResidenceNewsReqest residenceNews) {
        return null;
    }

    @ApiOperation(value = "Удаление новости по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResidenceNews(@PathVariable UUID id) {
        return null;
    }
}
