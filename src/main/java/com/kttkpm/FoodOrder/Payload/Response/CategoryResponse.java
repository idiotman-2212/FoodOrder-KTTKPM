package com.kttkpm.FoodOrder.Payload.Response;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class CategoryResponse {
    private int id;
    private String name;
    private Date createDate;
    private List<ProductResponse> products;

    public CategoryResponse() {
    }
    public CategoryResponse(int id, String name, Date createDate, List<ProductResponse> products) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.products = products;
    }

    public CategoryResponse(int id, String name) {
    }

}
