package com.taskmate.pojos;

import com.taskmate.entity.Task;

public class TaskResponse {
    
    private Task result;
    private String message;
    private boolean success;
    
    public TaskResponse() {}
    
    public TaskResponse(Task result, String message, boolean success) {
        this.result = result;
        this.message = message;
        this.success = success;
    }
    
    public static TaskResponse success(Task result) {
        return new TaskResponse(result, "Operation successful", true);
    }
    
    public static TaskResponse success(Task result, String message) {
        return new TaskResponse(result, message, true);
    }
    
    // Getters and Setters
    public Task getResult() {
        return result;
    }
    
    public void setResult(Task result) {
        this.result = result;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
