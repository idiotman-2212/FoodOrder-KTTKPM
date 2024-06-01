package com.kttkpm.FoodOrder.Payload.Response;

import lombok.*;

import java.util.Date;
import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryResponse {
    private int id;
    private String name;
    private Date createDate;
    private List<ProductResponse> products;


}
