package com.muhammetakduman.order_inventory;

public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException(String message){
        super((message));
    }
}
