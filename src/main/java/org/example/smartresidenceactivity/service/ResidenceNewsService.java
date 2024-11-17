package org.example.smartresidenceactivity.service;

import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.entity.ResidenceNews;
import org.example.smartresidenceactivity.exception.ErrorType;
import org.example.smartresidenceactivity.exception.ServiceException;
import org.example.smartresidenceactivity.mapper.ResidenceNewsRequestToResidenceNewsMapper;
import org.example.smartresidenceactivity.model.reqest.ResidenceNewsRequest;
import org.example.smartresidenceactivity.model.response.ResidenceNewsResponse;
import org.example.smartresidenceactivity.repository.ResidenceNewsRepository;
import org.example.smartresidenceactivity.util.ErrorMessageConstants;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResidenceNewsService {
    private final ResidenceNewsRepository residenceNewsRepository;
    private final ValidationService validationService;
    private final ConversionService conversionService;
    private final JwtService jwtService;
    private final ResidenceNewsRequestToResidenceNewsMapper residenceNewsRequestToResidenceNewsMapper;

    @Transactional
    public ResidenceNewsResponse createResidenceNews(ResidenceNewsRequest reqest) {
        var residenceNews = conversionService.convert(reqest, ResidenceNews.class);
        return conversionService.convert(residenceNewsRepository.save(residenceNews), ResidenceNewsResponse.class);
    }

    @Transactional(readOnly = true)
    public ResidenceNewsResponse getResidenceNewsById(UUID id) {
        validationService.validateProfileResidence(id);
        var residenceNews = residenceNewsRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_RESIDENCE_NEWS_NOT_FOUND, id))
        );
        return conversionService.convert(residenceNews, ResidenceNewsResponse.class);
    }

    @Transactional(readOnly = true)
    public List<ResidenceNewsResponse> getAllResidenceNews(UUID residenceId) {
        validationService.validateProfileResidence(residenceId);
        var residenceNewsList = residenceNewsRepository.findAllByResidenceId(residenceId);
        return residenceNewsList.stream().map(
                residenceNews -> conversionService.convert(residenceNews, ResidenceNewsResponse.class)
        ).toList();
    }

    @Transactional
    public ResidenceNewsResponse updateResidenceNews(UUID id, ResidenceNewsRequest reqest) {
        var residenceNewsForUpdate = residenceNewsRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_RESIDENCE_NEWS_NOT_FOUND, id)));
        residenceNewsForUpdate = residenceNewsRequestToResidenceNewsMapper.toResidenceNewsForUpdate(residenceNewsForUpdate, reqest);
        if (!(residenceNewsForUpdate.getCreatedBy().getId().equals(jwtService.getCurrentUserId()))) {
            throw new ServiceException(ErrorType.FORBIDDEN, String.format(ErrorMessageConstants.MSG_RESIDENCE_NEWS_FORBIDDEN, id));
        }
        return conversionService.convert(residenceNewsRepository.save(residenceNewsForUpdate), ResidenceNewsResponse.class);
    }

    @Transactional
    public void deleteResidenceNews(UUID id) {
        var residenceNews = residenceNewsRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_RESIDENCE_NEWS_NOT_FOUND, id))
        );
        if (!(residenceNews.getCreatedBy().getId().equals(jwtService.getCurrentUserId()))) {
            throw new ServiceException(ErrorType.FORBIDDEN, String.format(ErrorMessageConstants.MSG_RESIDENCE_NEWS_FORBIDDEN, id));
        }
        residenceNewsRepository.deleteById(id);
    }
}
