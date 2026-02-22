package com.taskmate.pojos;

import java.time.LocalDateTime;
import java.util.List;

public class SystemError {
    
    private String error;
    private String errorCode;
    private String message;
    private String details;
    private LocalDateTime timestamp;
    private String path;
    private List<String> validationErrors;
    
    public SystemError() {
        this.timestamp = LocalDateTime.now();
    }
    
    public SystemError(String error, String message) {
        this();
        this.error = error;
        this.message = message;
    }
    
    public SystemError(String error, String errorCode, String message) {
        this(error, message);
        this.errorCode = errorCode;
    }
    
    public SystemError(String error, String errorCode, String message, String details) {
        this(error, errorCode, message);
        this.details = details;
    }
    
    public static SystemError notFound(String message) {
        return new SystemError("NOT_FOUND", "RESOURCE_NOT_FOUND", message);
    }
    
    public static SystemError badRequest(String message) {
        return new SystemError("BAD_REQUEST", "INVALID_REQUEST", message);
    }
    
    public static SystemError internalError(String message) {
        return new SystemError("INTERNAL_ERROR", "SERVER_ERROR", message);
    }
    
    public static SystemError validationError(String message, List<String> validationErrors) {
        SystemError error = new SystemError("VALIDATION_ERROR", "INVALID_INPUT", message);
        error.setValidationErrors(validationErrors);
        return error;
    }
    
    public static SystemError unauthorized(String message) {
        return new SystemError("UNAUTHORIZED", "ACCESS_DENIED", message);
    }
    
    public static SystemError forbidden(String message) {
        return new SystemError("FORBIDDEN", "ACCESS_FORBIDDEN", message);
    }
    
    // Getters and Setters
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public List<String> getValidationErrors() {
        return validationErrors;
    }
    
    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
