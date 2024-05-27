package com.kttkpm.FoodOrder.Service;

import com.kttkpm.FoodOrder.Entity.FavouriteEntity;
import com.kttkpm.FoodOrder.Payload.Response.FavouriteResponse;
import com.kttkpm.FoodOrder.Repository.FavouriteRepository;
import com.kttkpm.FoodOrder.Service.Imp.FavouriteServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FavouriteService implements FavouriteServiceImp {
    @Autowired
    private FavouriteRepository favouriteRepository;

    @Override
    public List<FavouriteResponse> findAll() {
        List<FavouriteEntity> list = favouriteRepository.findAll();
        List<FavouriteResponse> listResponse = new ArrayList<>();

        for (FavouriteEntity data: list) {
            FavouriteResponse response = new FavouriteResponse();
            response.setId(data.getId());
            response.setProductId(data.getProduct().getId());
            response.setUserId(data.getUser().getId());
            response.setCreateDate(data.getCreateDate());

            listResponse.add(response);
        }
        return listResponse;
    }

    @Override
    public  List<FavouriteResponse> findById(int favouriteId) {
        Optional<FavouriteEntity> optional = favouriteRepository.findById(favouriteId);

        if (optional.isPresent()) {
            FavouriteEntity favouriteEntity  = optional.get();
            FavouriteResponse favouriteResponse = new FavouriteResponse();
            favouriteResponse.setId(favouriteEntity.getId());
            favouriteResponse.setProductId(favouriteEntity.getProduct().getId());
            favouriteResponse.setUserId(favouriteEntity.getUser().getId());
            favouriteResponse.setCreateDate(favouriteEntity.getCreateDate());

            List<FavouriteResponse> responseList = new ArrayList<>();

            responseList.add(favouriteResponse);
            return responseList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean save(int idProduct, int idUser) {
        FavouriteEntity favourite = new FavouriteEntity();
//        favourite.setUserId(idUser);
//        favourite.setProductId(idProduct);
        favourite.setCreateDate(new Date());

        favouriteRepository.save(favourite);
        return true;
    }

    @Override
    public boolean update(int idProduct, int idUser) {
        FavouriteEntity favourite = new FavouriteEntity();
//        favourite.setUserId(idUser);
//        favourite.setProductId(idProduct);
        favourite.setCreateDate(new Date());

        favouriteRepository.save(favourite);
        return true;
    }

    @Override
    public boolean deleteById(int favouriteId) {
        if (favouriteRepository.existsById(favouriteId)) {

            favouriteRepository.deleteById(favouriteId);
            return true;
        } else {
            return false;
        }
    }
}