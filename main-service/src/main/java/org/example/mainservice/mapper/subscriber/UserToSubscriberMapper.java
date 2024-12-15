package org.example.mainservice.mapper.subscriber;

import jakarta.annotation.Nonnull;
import org.example.mainservice.config.MapperConfiguration;
import org.example.mainservice.entity.Subscriber;
import org.example.mainservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperConfiguration.class)
public interface UserToSubscriberMapper extends Converter<User, Subscriber> {
    @Override
    @Mapping(target = "residenceId", source = "residence.id")
    @Mapping(target = "id", ignore = true)
    Subscriber convert(@Nonnull User source);
}
