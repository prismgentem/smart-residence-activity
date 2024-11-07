package org.example.smartresidenceactivity.service;

import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.reqest.ResidenceNewsReqest;
import org.example.smartresidenceactivity.model.response.ResidenceNewsResponse;
import org.example.smartresidenceactivity.repository.ResidenceNewsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResidenceNewsService {
    private final ResidenceNewsRepository residenceNewsRepository;
    private final ValidationService validationService;

    @Transactional
    public ResidenceNewsResponse createResidenceNews(ResidenceNewsReqest residenceNews) {
        return null;
    }

    @Transactional(readOnly = true)
    public ResidenceNewsResponse getResidenceNewsById(UUID id) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<ResidenceNewsResponse> getAllResidenceNews() {
        return null;
    }

    @Transactional
    public ResidenceNewsResponse updateResidenceNews(UUID id, ResidenceNewsReqest residenceNews) {
        return null;
    }

    @Transactional
    public void deleteResidenceNews(UUID id) {
    }
}
