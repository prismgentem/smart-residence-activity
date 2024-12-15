package org.example.mainservice.service;

import org.example.mainservice.entity.Admin;
import org.example.mainservice.entity.Residence;
import org.example.mainservice.entity.ResidenceNews;
import org.example.mainservice.entity.User;
import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.mapper.residencenews.ResidenceNewsRequestToResidenceNewsMapper;
import org.example.mainservice.model.reqest.ResidenceNewsRequest;
import org.example.mainservice.model.reqest.ShortResidenceRequest;
import org.example.mainservice.model.response.ResidenceNewsResponse;
import org.example.mainservice.repository.AdminRepository;
import org.example.mainservice.repository.ResidenceNewsRepository;
import org.example.mainservice.repository.UserRepository;
import org.example.mainservice.service.AccessService;
import org.example.mainservice.service.JwtService;
import org.example.mainservice.service.NotificationService;
import org.example.mainservice.service.ResidenceNewsService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ResidenceNewsServiceTest {

    @Mock
    private ResidenceNewsRepository residenceNewsRepository;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private ConversionService conversionService;
    @Mock
    private AccessService accessService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private ResidenceNewsRequestToResidenceNewsMapper residenceNewsRequestToResidenceNewsMapper;

    @InjectMocks
    private ResidenceNewsService residenceNewsService;

    private ResidenceNewsRequest residenceNewsRequest;
    private ResidenceNews residenceNews;
    private ResidenceNewsResponse residenceNewsResponse;
    private UUID residenceNewsId;
    private UUID adminId;
    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        easyRandom = new EasyRandom();
        residenceNewsId = UUID.randomUUID();
        adminId = UUID.randomUUID();

        residenceNewsRequest = ResidenceNewsRequest.builder()
                .sendNotification(true)
                .residence(ShortResidenceRequest.builder()
                        .id(UUID.randomUUID())
                        .build())
                .build();

        residenceNews = easyRandom.nextObject(ResidenceNews.class);
        residenceNews.setId(residenceNewsId);
        residenceNews.setSendNotification(true);
        residenceNewsResponse = easyRandom.nextObject(ResidenceNewsResponse.class);
    }

    @Test
    void createResidenceNews_ShouldReturnResidenceNewsResponseAndSandNotification() {
        // Arrange
        var admin = easyRandom.nextObject(Admin.class);
        admin.getResidence().setId(residenceNewsRequest.getResidence().getId());

        var residenceNews = ResidenceNews.builder()
                .residence(admin.getResidence())
                .createdBy(admin)
                .sendNotification(true)
                .build();

        when(jwtService.getCurrentUserId()).thenReturn(admin.getId());
        when(adminRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(conversionService.convert(residenceNewsRequest, ResidenceNews.class)).thenReturn(residenceNews);
        when(residenceNewsRepository.save(residenceNews)).thenReturn(residenceNews);
        when(conversionService.convert(residenceNews, ResidenceNewsResponse.class)).thenReturn(residenceNewsResponse);
        var user = easyRandom.nextObject(User.class);
        user.setEmail("test@test.com");
        user.setTelegramUsername("test");
        when(userRepository.findAllByResidenceId(residenceNewsRequest.getResidence().getId())).thenReturn(List.of(user));

        // Act
        var result = residenceNewsService.createResidenceNews(residenceNewsRequest);

        // Assert
        assertEquals(residenceNewsResponse, result);
        verify(notificationService).sendResidenceNewsEmailNotification(user.getTelegramUsername(), user.getEmail(), residenceNews);
        verify(residenceNewsRepository, times(1)).save(residenceNews);
        verify(notificationService, times(1)).sendResidenceNewsEmailNotification(user.getTelegramUsername(), user.getEmail(), residenceNews);
    }

    @Test
    void createResidenceNews_ShouldReturnResidenceNewsResponseWithoutNotification() {
        // Arrange
        var admin = easyRandom.nextObject(Admin.class);
        admin.getResidence().setId(residenceNewsRequest.getResidence().getId());

        var residenceNews = ResidenceNews.builder()
                .residence(admin.getResidence())
                .createdBy(admin)
                .build();

        when(jwtService.getCurrentUserId()).thenReturn(admin.getId());
        when(adminRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(conversionService.convert(residenceNewsRequest, ResidenceNews.class)).thenReturn(residenceNews);
        when(residenceNewsRepository.save(residenceNews)).thenReturn(residenceNews);
        when(conversionService.convert(residenceNews, ResidenceNewsResponse.class)).thenReturn(residenceNewsResponse);

        // Act
        var result = residenceNewsService.createResidenceNews(residenceNewsRequest);

        // Assert
        assertEquals(residenceNewsResponse, result);
        verify(residenceNewsRepository, times(1)).save(residenceNews);
        verify(notificationService, never()).sendResidenceNewsEmailNotification(any(), any(), any());
    }

    @Test
    void createResidenceNews_ShouldReturnForbidden() {
        // Arrange
        var residence = Residence.builder()
                .id(UUID.randomUUID())
                .build();

        var admin = easyRandom.nextObject(Admin.class);
        admin.setResidence(residence);

        var residenceForNews = ShortResidenceRequest.builder()
                .id(UUID.randomUUID())
                .build();

        residenceNewsRequest.setResidence(residenceForNews);

        when(jwtService.getCurrentUserId()).thenReturn(admin.getId());
        when(conversionService.convert(residenceNewsRequest, ResidenceNews.class)).thenReturn(residenceNews);
        when(adminRepository.findById(admin.getId())).thenReturn(Optional.of(admin));


        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            residenceNewsService.createResidenceNews(residenceNewsRequest);
        });

        // Assert
        verify(adminRepository).findById(admin.getId());
        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());
        }

    @Test
    void getResidenceNewsById_ShouldReturnResidenceNewsResponse() {
        // Arrange
        when(residenceNewsRepository.findById(residenceNewsId)).thenReturn(Optional.of(residenceNews));
        when(conversionService.convert(residenceNews, ResidenceNewsResponse.class)).thenReturn(residenceNewsResponse);
        when(jwtService.getCurrentUserId()).thenReturn(adminId);

        // Act
        var result = residenceNewsService.getResidenceNewsById(residenceNewsId);

        // Assert
        assertEquals(residenceNewsResponse, result);
        verify(accessService).checkAccessResidenceAwareToResidence(residenceNews.getResidence().getId());
        verify(residenceNewsRepository).findById(residenceNewsId);
    }

    @Test
    void getResidenceNewsById_ShouldNotFound() {
        // Arrange
        when(residenceNewsRepository.findById(residenceNewsId)).thenReturn(Optional.empty());


        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            residenceNewsService.getResidenceNewsById(residenceNewsId);
        });

        // Assert
        verify(residenceNewsRepository).findById(residenceNewsId);
        assertEquals(ErrorType.NOT_FOUND.getCode(), exception.getCode());
    }

    @Test
    void getAllResidenceNews_ShouldListReturnResidenceNewsResponse() {
        // Arrange
        var residenceId = UUID.randomUUID();
        var residence = Residence.builder()
                .id(residenceId)
                .build();
        residenceNews.setResidence(residence);
        var admin = easyRandom.nextObject(Admin.class);
        admin.setId(UUID.randomUUID());
        admin.setResidence(residence);

        residenceNewsRepository.save(ResidenceNews.builder().residence(
                        Residence.builder()
                                .id(UUID.randomUUID()).build())
                .build());

        when(jwtService.getCurrentUserId()).thenReturn(admin.getId());
        when(adminRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(residenceNewsRepository.findAllByResidenceId(residenceId)).thenReturn(List.of(residenceNews));

        // Act
        var result = residenceNewsService.getAllResidenceNews(residenceNews.getResidence().getId());

        // Assert
        assertEquals(result.size(), 1);
        verify(accessService).checkAccessResidenceAwareToResidence(residenceNews.getResidence().getId());
        verify(residenceNewsRepository).findAllByResidenceId(residenceId);
    }

    @Test
    void updateResidenceNews_ShouldReturnUpdatedResidenceNewsResponse() {
        // Arrange
        var updatedNews = easyRandom.nextObject(ResidenceNews.class);
        updatedNews.setCreatedBy(Admin.builder().id(adminId).build());

        when(residenceNewsRepository.findById(residenceNewsId)).thenReturn(Optional.of(residenceNews));
        when(residenceNewsRequestToResidenceNewsMapper.toResidenceNewsForUpdate(residenceNews, residenceNewsRequest)).thenReturn(updatedNews);
        when(residenceNewsRepository.save(updatedNews)).thenReturn(updatedNews);
        when(conversionService.convert(updatedNews, ResidenceNewsResponse.class)).thenReturn(residenceNewsResponse);
        when(jwtService.getCurrentUserId()).thenReturn(adminId);

        // Act
        var result = residenceNewsService.updateResidenceNews(residenceNewsId, residenceNewsRequest);

        // Assert
        assertEquals(residenceNewsResponse, result);
        verify(residenceNewsRepository).findById(residenceNewsId);
        verify(residenceNewsRepository).save(updatedNews);
    }

    @Test
    void updateResidenceNews_ShouldReturnForbidden() {
        // Arrange
        var updatedNews = easyRandom.nextObject(ResidenceNews.class);
        updatedNews.setCreatedBy(Admin.builder().id(UUID.randomUUID()).build());

        when(residenceNewsRepository.findById(residenceNewsId)).thenReturn(Optional.of(residenceNews));
        when(residenceNewsRequestToResidenceNewsMapper.toResidenceNewsForUpdate(residenceNews, residenceNewsRequest)).thenReturn(updatedNews);
        when(residenceNewsRepository.save(updatedNews)).thenReturn(updatedNews);
        when(conversionService.convert(updatedNews, ResidenceNewsResponse.class)).thenReturn(residenceNewsResponse);
        when(jwtService.getCurrentUserId()).thenReturn(adminId);

        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            residenceNewsService.updateResidenceNews(residenceNewsId, residenceNewsRequest);
        });

        // Assert
        verify(residenceNewsRepository).findById(residenceNewsId);
        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());
    }

    @Test
    void updateResidenceNews_ShouldReturnNotFound() {
        // Arrange
        var updatedNews = easyRandom.nextObject(ResidenceNews.class);
        updatedNews.setCreatedBy(Admin.builder().id(adminId).build());

        when(residenceNewsRepository.findById(residenceNewsId)).thenReturn(Optional.empty());

        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            residenceNewsService.updateResidenceNews(residenceNewsId, residenceNewsRequest);
        });

        // Assert
        verify(residenceNewsRepository).findById(residenceNewsId);
        assertEquals(ErrorType.NOT_FOUND.getCode(), exception.getCode());
    }

    @Test
    void deleteResidenceNews_ShouldDeleteNews() {
        // Arrange
        var residenceNews = ResidenceNews.builder()
                .id(residenceNewsId)
                .createdBy(Admin.builder().id(adminId).build())
                .build();

        when(residenceNewsRepository.findById(residenceNewsId)).thenReturn(Optional.of(residenceNews));
        when(jwtService.getCurrentUserId()).thenReturn(adminId);

        // Act
        residenceNewsService.deleteResidenceNews(residenceNewsId);

        // Assert
        verify(residenceNewsRepository).deleteById(residenceNewsId);
    }

    @Test
    void deleteResidenceNews_ShouldReturnForbidden() {
        // Arrange
        var residenceNews = ResidenceNews.builder()
                .id(residenceNewsId)
                .createdBy(Admin.builder().id(UUID.randomUUID()).build())
                .build();

        when(residenceNewsRepository.findById(residenceNewsId)).thenReturn(Optional.of(residenceNews));
        when(jwtService.getCurrentUserId()).thenReturn(adminId);

        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            residenceNewsService.deleteResidenceNews(residenceNewsId);
        });

        // Assert
        verify(residenceNewsRepository).findById(residenceNewsId);
        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());
    }

    @Test
    void deleteResidenceNews_ShouldReturnNotFound() {
        // Arrange
        var updatedNews = easyRandom.nextObject(ResidenceNews.class);
        updatedNews.setCreatedBy(Admin.builder().id(adminId).build());

        when(residenceNewsRepository.findById(residenceNewsId)).thenReturn(Optional.empty());

        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            residenceNewsService.deleteResidenceNews(residenceNewsId);
        });

        // Assert
        verify(residenceNewsRepository).findById(residenceNewsId);
        assertEquals(ErrorType.NOT_FOUND.getCode(), exception.getCode());
    }

    @Test
    void createResidenceNews_ShouldThrowServiceException_WhenAdminNotFound() {
        // Arrange
        when(jwtService.getCurrentUserId()).thenReturn(UUID.randomUUID());
        when(adminRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(ServiceException.class,
                () -> residenceNewsService.createResidenceNews(residenceNewsRequest));

        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
    }
}
