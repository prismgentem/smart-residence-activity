package org.example.mainservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

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