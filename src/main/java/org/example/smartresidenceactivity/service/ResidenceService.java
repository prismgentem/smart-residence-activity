package org.example.smartresidenceactivity.service;

import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.entity.Residence;
import org.example.smartresidenceactivity.exception.ErrorType;
import org.example.smartresidenceactivity.exception.ServiceException;
import org.example.smartresidenceactivity.mapper.ResidenceRequestToResidenceMapper;
import org.example.smartresidenceactivity.model.reqest.ResidenceRequest;
import org.example.smartresidenceactivity.model.response.ResidenceResponse;
import org.example.smartresidenceactivity.repository.AdminRepository;
import org.example.smartresidenceactivity.repository.ResidenceRepository;
import org.example.smartresidenceactivity.util.ErrorMessageConstants;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResidenceService {
    private final ResidenceRepository residenceRepository;
    private final ValidationService validationService;
    private final ConversionService conversionService;
    private final JwtService jwtService;
    private final AdminRepository adminRepository;
    private final ResidenceRequestToResidenceMapper residenceRequestToResidenceMapper;

    @Transactional
    public ResidenceResponse createResidence(ResidenceRequest residence) {
        var residenceEntity = conversionService.convert(residence, Residence.class);
        return conversionService.convert(residenceRepository.save(residenceEntity), ResidenceResponse.class);
    }

    @Transactional(readOnly = true)
    public ResidenceResponse getResidenceById(UUID id) {
        var residence = residenceRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_RESIDENCE_NOT_FOUND, id))
        );
        return conversionService.convert(residence, ResidenceResponse.class);
    }

    @Transactional(readOnly = true)
    public List<ResidenceResponse> getAllResidences() {
        var residenceList = residenceRepository.findAll();
        return residenceList.stream().map(
                residence -> conversionService.convert(residence, ResidenceResponse.class)
        ).toList();
    }

    @Transactional
    public ResidenceResponse updateResidence(UUID id, ResidenceRequest request) {
        var admin = adminRepository.findById(jwtService.getCurrentUserId()).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_RESIDENCE_FORBIDDEN, id))
        );
        if (!admin.getResidence().getId().equals(id)) {
            throw new ServiceException(ErrorType.FORBIDDEN, String.format(ErrorMessageConstants.MSG_RESIDENCE_FORBIDDEN, id));
        }
        var residenceForUpdate = residenceRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_RESIDENCE_NOT_FOUND, id))
        );
        residenceForUpdate = residenceRequestToResidenceMapper.toResidenceForUpdate(residenceForUpdate, request);
        return conversionService.convert(residenceRepository.save(residenceForUpdate), ResidenceResponse.class);

    }

    @Transactional
    public void deleteResidence(UUID id) {
        residenceRepository.deleteById(id);
    }
}
