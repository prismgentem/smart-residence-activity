package org.example.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.repository.AdminRepository;
import org.example.mainservice.repository.ResidenceNewsRepository;
import org.example.mainservice.repository.UserRepository;
import org.example.mainservice.entity.ResidenceNews;
import org.example.mainservice.mapper.residencenews.ResidenceNewsRequestToResidenceNewsMapper;
import org.example.mainservice.model.reqest.ResidenceNewsRequest;
import org.example.mainservice.model.response.ResidenceNewsResponse;
import org.example.mainservice.util.ErrorMessageConstants;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class ResidenceNewsService {
    private final ResidenceNewsRepository residenceNewsRepository;
    private final AccessService accessService;
    private final ConversionService conversionService;
    private final AdminRepository adminRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ResidenceNewsRequestToResidenceNewsMapper residenceNewsRequestToResidenceNewsMapper;

    @Transactional
    public ResidenceNewsResponse createResidenceNews(ResidenceNewsRequest request) {
        var residenceNews = conversionService.convert(request, ResidenceNews.class);
        var admin = adminRepository.findByEmail(jwtService.getCurrentUserEmail()).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST,
                        String.format(ErrorMessageConstants.MSG_ADMIN_NOT_FOUND, jwtService.getCurrentUserId()))
        );

        if (admin.getResidence().getId().equals(requireNonNull(residenceNews).getResidence().getId())) {
            residenceNews.setCreatedBy(admin);
        } else {
            throw new ServiceException(ErrorType.FORBIDDEN,
                    String.format(ErrorMessageConstants.MSG_RESIDENCE_NEWS_FORBIDDEN, residenceNews.getId()));
        }

        if (Boolean.TRUE.equals(residenceNews.getSendNotification())) {
            sendResidenceNewsNotificationsToUsers(residenceNews);
        }

        return conversionService.convert(residenceNewsRepository.save(residenceNews), ResidenceNewsResponse.class);
    }

    @Transactional(readOnly = true)
    public ResidenceNewsResponse getResidenceNewsById(UUID id) {
        var residenceNews = residenceNewsRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.NOT_FOUND, String.format(ErrorMessageConstants.MSG_RESIDENCE_NEWS_NOT_FOUND, id))
        );
        accessService.checkAccessResidenceAwareToResidence(residenceNews.getResidence().getId());
        return conversionService.convert(residenceNews, ResidenceNewsResponse.class);
    }

    @Transactional(readOnly = true)
    public List<ResidenceNewsResponse> getAllResidenceNews(UUID residenceId) {
        accessService.checkAccessResidenceAwareToResidence(residenceId);
        var residenceNewsList = residenceNewsRepository.findAllByResidenceId(residenceId);
        return residenceNewsList.stream().map(
                residenceNews -> conversionService.convert(residenceNews, ResidenceNewsResponse.class)
        ).toList();
    }

    @Transactional
    public ResidenceNewsResponse updateResidenceNews(UUID id, ResidenceNewsRequest request) {
        var residenceNewsForUpdate = residenceNewsRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.NOT_FOUND, String.format(ErrorMessageConstants.MSG_RESIDENCE_NEWS_NOT_FOUND, id)));
        residenceNewsForUpdate = residenceNewsRequestToResidenceNewsMapper.toResidenceNewsForUpdate(residenceNewsForUpdate, request);
        if (!(residenceNewsForUpdate.getCreatedBy().getId().equals(jwtService.getCurrentUserId()))) {
            throw new ServiceException(ErrorType.FORBIDDEN, String.format(ErrorMessageConstants.MSG_RESIDENCE_NEWS_FORBIDDEN, id));
        }
        return conversionService.convert(residenceNewsRepository.save(residenceNewsForUpdate), ResidenceNewsResponse.class);
    }

    @Transactional
    public void deleteResidenceNews(UUID id) {
        var residenceNews = residenceNewsRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.NOT_FOUND, String.format(ErrorMessageConstants.MSG_RESIDENCE_NEWS_NOT_FOUND, id))
        );
        if (!(residenceNews.getCreatedBy().getId().equals(jwtService.getCurrentUserId()))) {
            throw new ServiceException(ErrorType.FORBIDDEN, String.format(ErrorMessageConstants.MSG_RESIDENCE_NEWS_FORBIDDEN, id));
        }
        residenceNewsRepository.deleteById(id);
    }

    private void sendResidenceNewsNotificationsToUsers(ResidenceNews residenceNews) {
        var users = userRepository.findAllByResidenceId(residenceNews.getResidence().getId());
        if (!users.isEmpty()) {
            users.forEach(user -> notificationService.sendResidenceNewsEmailNotification(
                    user.getTelegramUsername(),
                    user.getEmail(),
                    residenceNews
            ));
        }
    }

}
