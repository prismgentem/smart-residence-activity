package org.example.mainservice.mapper.servicerequest;

import jakarta.annotation.Nonnull;
import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.ServiceRequest;
import org.example.mainservice.mapper.user.UserToShortUserResponseMapper;
import org.example.mainservice.model.response.ServiceRequestV1Response;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class, uses = {UserToShortUserResponseMapper.class})
public interface ServiceRequestToServiceRequestV1ResponseMapper extends Converter<ServiceRequest, ServiceRequestV1Response> {
    @Override
    ServiceRequestV1Response convert(@Nonnull ServiceRequest source);
}
