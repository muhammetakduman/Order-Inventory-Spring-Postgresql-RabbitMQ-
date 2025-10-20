package com.muhammetakduman.order_inventory.dto.order;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        T data,
        String message,
        Instant timestamp
) {
    public static <T> ApiResponse<T> ok (T data){
        return new ApiResponse<>(true, data , null , Instant.now());
    }
    public static <T> ApiResponse<T> msg(String message){
        return new ApiResponse<>(true,null,message,Instant.now());
    }
}
