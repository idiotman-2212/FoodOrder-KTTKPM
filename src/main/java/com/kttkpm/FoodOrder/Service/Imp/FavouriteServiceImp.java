package com.kttkpm.FoodOrder.Service.Imp;

import com.kttkpm.FoodOrder.Payload.Response.FavouriteResponse;

import java.util.List;

public interface FavouriteServiceImp {
    List<FavouriteResponse> findAll();
    List<FavouriteResponse> findById( int favouriteId);
    boolean save(int idProduct, int idUser);
    boolean update(int idProduct, int idUser);
    boolean deleteById( int favouriteId);
}