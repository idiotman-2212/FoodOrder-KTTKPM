package com.kttkpm.FoodOrder.Payload.Response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseResponse {
    private int statusCode;
    private String message;
    private Object data;


}