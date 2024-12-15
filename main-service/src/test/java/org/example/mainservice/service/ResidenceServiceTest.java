package org.example.mainservice.service;

import org.example.mainservice.entity.Admin;
import org.example.mainservice.entity.Residence;
import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.mapper.residence.ResidenceRequestToResidenceMapper;
import org.example.mainservice.model.reqest.ResidenceRequest;
import org.example.mainservice.model.response.ResidenceResponse;
import org.example.mainservice.repository.AdminRepository;
import org.example.mainservice.repository.ResidenceRepository;
import org.example.mainservice.service.JwtService;
import org.example.mainservice.service.ResidenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ResidenceServiceTest {

    @Mock
    private ResidenceRepository residenceRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private ResidenceRequestToResidenceMapper residenceRequestToResidenceMapper;

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private ResidenceService residenceService;

    private UUID testResidenceId;
    private UUID testAdminId;
    private Residence testResidence;
    private Admin testAdmin;
    private ResidenceRequest testResidenceRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testResidenceId = UUID.randomUUID();
        testAdminId = UUID.randomUUID();

        testResidence = new Residence();
        testResidence.setId(testResidenceId);

        testAdmin = new Admin();
        testAdmin.setResidence(testResidence);

        testResidenceRequest = new ResidenceRequest();
    }

    @Test
    void createResidence_ShouldReturnResidenceResponse() {
        // Arrange
        when(conversionService.convert(testResidenceRequest, Residence.class)).thenReturn(testResidence);
        when(residenceRepository.save(testResidence)).thenReturn(testResidence);
        when(conversionService.convert(testResidence, ResidenceResponse.class)).thenReturn(new ResidenceResponse());

        // Act
        ResidenceResponse response = residenceService.createResidence(testResidenceRequest);

        // Assert
        assertNotNull(response);
        verify(residenceRepository).save(testResidence);
    }

    @Test
    void getResidenceById_ShouldReturnResidenceResponse() {
        // Arrange
        when(residenceRepository.findById(testResidenceId)).thenReturn(Optional.of(testResidence));
        when(conversionService.convert(testResidence, ResidenceResponse.class)).thenReturn(new ResidenceResponse());

        // Act
        ResidenceResponse response = residenceService.getResidenceById(testResidenceId);

        // Assert
        assertNotNull(response);
        verify(residenceRepository).findById(testResidenceId);
    }

    @Test
    void getResidenceById_ShouldThrowServiceException_WhenResidenceNotFound() {
        // Arrange
        when(residenceRepository.findById(testResidenceId)).thenReturn(Optional.empty());

        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            residenceService.getResidenceById(testResidenceId);
        });

        // Assert
        assertEquals(ErrorType.BAD_REQUEST.getCode(), exception.getCode());
    }

    @Test
    void getAllResidences_ShouldReturnResidenceResponseList() {
        // Arrange
        List<Residence> residences = List.of(
                Residence.builder().id(UUID.randomUUID()).name("Residence 1").build(),
                Residence.builder().id(UUID.randomUUID()).name("Residence 2").build()
        );

        List<ResidenceResponse> residenceResponses = residences.stream()
                .map(residence -> ResidenceResponse.builder().id(residence.getId()).name(residence.getName()).build())
                .toList();

        when(residenceRepository.findAll()).thenReturn(residences);
        for (int i = 0; i < residences.size(); i++) {
            when(conversionService.convert(residences.get(i), ResidenceResponse.class)).thenReturn(residenceResponses.get(i));
        }

        // Act
        List<ResidenceResponse> result = residenceService.getAllResidences();

        // Assert
        assertNotNull(result);
        assertEquals(residences.size(), result.size());
        for (int i = 0; i < residences.size(); i++) {
            assertEquals(residences.get(i).getId(), result.get(i).getId());
            assertEquals(residences.get(i).getName(), result.get(i).getName());
        }
        verify(residenceRepository).findAll();
    }


    @Test
    void updateResidence_ShouldReturnUpdatedResidenceResponse() {
        // Arrange
        testAdmin.setResidence(testResidence);

        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(adminRepository.findById(testAdminId)).thenReturn(Optional.of(testAdmin));
        when(residenceRepository.findById(testResidenceId)).thenReturn(Optional.of(testResidence));
        when(residenceRequestToResidenceMapper.toResidenceForUpdate(testResidence, testResidenceRequest)).thenReturn(testResidence);
        when(residenceRepository.save(testResidence)).thenReturn(testResidence);
        when(conversionService.convert(testResidence, ResidenceResponse.class)).thenReturn(new ResidenceResponse());

        // Act
        ResidenceResponse response = residenceService.updateResidence(testResidenceId, testResidenceRequest);

        // Assert
        assertNotNull(response);
        verify(residenceRepository).save(testResidence);
    }


    @Test
    void updateResidence_ShouldThrowServiceException_WhenAdminHasNoAccess() {
        // Arrange
        when(jwtService.getCurrentUserId()).thenReturn(testAdminId);
        when(adminRepository.findById(testAdminId)).thenReturn(Optional.of(Admin.builder()
                        .residence(Residence.builder()
                                .id(UUID.randomUUID())
                                .build())
                .id(testAdminId)
                .build()));
        when(residenceRepository.findById(testResidenceId)).thenReturn(Optional.of(testResidence));

        // Act
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            residenceService.updateResidence(testResidenceId, testResidenceRequest);
        });

        // Assert
        assertEquals(ErrorType.FORBIDDEN.getCode(), exception.getCode());
    }

    @Test
    void deleteResidence_ShouldDeleteResidence() {
        // Arrange
        when(residenceRepository.existsById(testResidenceId)).thenReturn(true);

        // Act
        residenceService.deleteResidence(testResidenceId);

        // Assert
        verify(residenceRepository).deleteById(testResidenceId);
    }
}

