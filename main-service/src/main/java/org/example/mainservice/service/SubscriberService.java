package org.example.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.example.mainservice.entity.Subscriber;
import org.example.mainservice.exception.ErrorType;
import org.example.mainservice.exception.ServiceException;
import org.example.mainservice.repository.ResidenceRepository;
import org.example.mainservice.repository.SubscriberRepository;
import org.example.mainservice.repository.UserRepository;
import org.example.mainservice.util.ErrorMessageConstants;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ConversionService conversionService;

    @Transactional
    public void subscribeToEventsNearResidence() {
        var email = jwtService.getCurrentUserEmail();
        var user = userRepository.findByEmail(jwtService.getCurrentUserEmail()).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_USER_NOT_FOUND, email))
        );

        if (subscriberRepository.existsByEmail(email)) {
            throw new ServiceException(ErrorType.CONFLICT, String.format(ErrorMessageConstants.MSG_SUBSCRIBER_ALREADY_EXISTS, email));
        }

        var subscriber = conversionService.convert(user, Subscriber.class);
        subscriberRepository.save(requireNonNull(subscriber));
    }

    @Transactional
    public void unsubscribeToEventsNearResidence() {
        var email = jwtService.getCurrentUserEmail();
        userRepository.findByEmail(jwtService.getCurrentUserEmail()).orElseThrow(
                () -> new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_USER_NOT_FOUND, email))
        );

        if (!subscriberRepository.existsByEmail(email)) {
            throw new ServiceException(ErrorType.BAD_REQUEST, String.format(ErrorMessageConstants.MSG_SUBSCRIBER_NOT_FOUND, email));
        }

        subscriberRepository.deleteByEmail(email);
    }
}
