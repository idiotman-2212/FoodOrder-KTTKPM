package com.kttkpm.FoodOrder.Helper;


import com.kttkpm.FoodOrder.Entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class GlobalData {
    //tao bien toan cuc
    public static List<ProductEntity> cart;

    static {
        cart = new ArrayList<>();
    }

}