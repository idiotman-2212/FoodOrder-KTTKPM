package com.kttkpm.FoodOrder.Payload.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FavouriteResponse {
    private int id;

    private Integer userId;

    private Integer productId;

    @JsonProperty("user")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserResponse userResponse;

    @JsonProperty("product")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProductResponse productResponse;

    private Date createDate;


}