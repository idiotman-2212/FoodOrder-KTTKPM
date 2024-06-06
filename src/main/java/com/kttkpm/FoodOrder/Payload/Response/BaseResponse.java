package com.kttkpm.FoodOrder.Payload.Response;

<<<<<<< HEAD
import lombok.Data;

@Data
=======
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
>>>>>>> origin/tai-dev
public class BaseResponse {
    private int statusCode;
    private String message;
    private Object data;

<<<<<<< HEAD
=======

>>>>>>> origin/tai-dev
}