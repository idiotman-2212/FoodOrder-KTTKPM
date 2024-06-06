package com.kttkpm.FoodOrder.Payload.Response;

<<<<<<< HEAD
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
=======
import lombok.*;

import java.util.Date;
import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
>>>>>>> origin/tai-dev
public class CategoryResponse {
    private int id;
    private String name;
    private Date createDate;
    private List<ProductResponse> products;


<<<<<<< HEAD
    public CategoryResponse(int id, String name) {
    }

=======
>>>>>>> origin/tai-dev
}
