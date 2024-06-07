package com.kttkpm.FoodOrder.Payload.Response;

import lombok.Data;

@Data
public class BaseResponse {
    private int statusCode;
    private String message;
    private Object data;

}