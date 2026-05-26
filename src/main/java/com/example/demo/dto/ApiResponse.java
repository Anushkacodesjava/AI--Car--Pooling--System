package com.example.demo.dto;
import java.time.LocalDateTime;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiResponse(boolean success, String message, T data){
        this.success=success; this.message=message; this.data=data;
    }
    public static <T> ApiResponse<T> ok(T data){ return new ApiResponse<>(true,"Success",data); }
    public static <T> ApiResponse<T> ok(String msg, T data){ return new ApiResponse<>(true,msg,data); }
    public static <T> ApiResponse<T> error(String msg){ return new ApiResponse<>(false,msg,null); }

    public boolean isSuccess(){return success;} public void setSuccess(boolean s){this.success=s;}
    public String getMessage(){return message;} public void setMessage(String m){this.message=m;}
    public T getData(){return data;} public void setData(T d){this.data=d;}
    public LocalDateTime getTimestamp(){return timestamp;}
}
