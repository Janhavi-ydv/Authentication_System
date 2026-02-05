package com.authsystem.authentication.exception;

import java.time.LocalDateTime;

public class ApiErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ApiErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
