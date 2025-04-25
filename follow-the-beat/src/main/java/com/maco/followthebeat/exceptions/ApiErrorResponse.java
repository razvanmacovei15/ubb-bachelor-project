package com.maco.followthebeat.exceptions;

import lombok.Getter;

@Getter
public class ApiErrorResponse {
    private final String message;
    private final Object details;

    public ApiErrorResponse(String message, Object details) {
        this.message = message;
        this.details = details;
    }

}