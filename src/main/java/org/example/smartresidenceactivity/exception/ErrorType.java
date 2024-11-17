package org.example.smartresidenceactivity.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "AccessDenied"),
    BAD_ARGUMENT(HttpStatus.BAD_REQUEST.value(), "BadArgument"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "BadRequest"),
    CONFLICT(HttpStatus.CONFLICT.value(), "Conflict"),
    GONE(HttpStatus.GONE.value(), "Gone"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "InternalServerError"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "MethodNotAllowed"),
    MISSING_PATH_VARIABLE(HttpStatus.BAD_REQUEST.value(), "MissingPathVariable"),
    MISSING_REQ_PARAMS(HttpStatus.BAD_REQUEST.value(), "MissingRequiredParams"),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "NotFound"),
    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE.value(), "PayloadTooLarge"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE.value(), "ServiceUnavailable"),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "UnsupportedMediaType"),
    LOCKED(HttpStatus.LOCKED.value(), "Locked"),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "Forbidden");

    private final int status;
    private final String code;
}
