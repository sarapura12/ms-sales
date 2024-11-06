package com.salemanager.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOperationException extends RuntimeException {
    private final String resourceName;
    private final Object resourceId;

    public InvalidOperationException(String resourceName, Object resourceId, String message) {
        super(message);
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

}
