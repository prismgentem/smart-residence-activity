package org.example.smartresidenceactivity.service;

import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.reqest.ResidenceRequest;
import org.example.smartresidenceactivity.model.response.ResidenceResponse;
import org.example.smartresidenceactivity.repository.ResidenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResidenceService {
    private final ResidenceRepository residenceRepository;
    private final ValidationService validationService;

    @Transactional
    public ResidenceResponse createResidence(ResidenceRequest residence) {
        return null;
    }

    @Transactional(readOnly = true)
    public ResidenceResponse getResidenceById(UUID id) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<ResidenceResponse> getAllResidences() {
        return null;
    }

    @Transactional
    public ResidenceResponse updateResidence(UUID id, ResidenceRequest residence) {
        return null;
    }

    @Transactional
    public void deleteResidence(UUID id) {
    }
}
