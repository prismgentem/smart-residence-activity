package org.example.mainservice.mapper.servicerequest;

import jakarta.annotation.Nonnull;
import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.ServiceRequest;
import org.example.mainservice.mapper.user.ShortUserRequestToUserMapper;
import org.example.mainservice.model.reqest.ServiceRequestV1Request;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class, uses = {ShortUserRequestToUserMapper.class})
public interface ServiceRequestV1RequestToServiceRequestMapper extends Converter<ServiceRequestV1Request, ServiceRequest> {
    @Override
    ServiceRequest convert(@Nonnull ServiceRequestV1Request source);

    ServiceRequest toServiceRequestForUpdate(@MappingTarget ServiceRequest target, ServiceRequestV1Request source);
}
