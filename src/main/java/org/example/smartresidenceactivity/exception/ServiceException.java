package org.example.smartresidenceactivity.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {
    private final Integer status;
    private final String code;
    public ServiceException(ErrorType errorType, String message) {
        super(message);
        this.status = errorType.getStatus();
        this.code = errorType.getCode();
    }
}