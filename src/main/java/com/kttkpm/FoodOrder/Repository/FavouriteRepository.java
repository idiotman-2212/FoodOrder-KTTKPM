package com.kttkpm.FoodOrder.Repository;

import com.kttkpm.FoodOrder.Entity.FavouriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteRepository extends JpaRepository<FavouriteEntity,Integer> {
}
