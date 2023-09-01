package com.svalero.gameshop_aa1_multimedia.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.gameshop_aa1_multimedia.domain.Shop;

import java.util.List;

@Dao
public interface ShopDAO {
    @Query("SELECT * FROM shop")
    List<Shop> getAll();

    @Query("SELECT * FROM shop WHERE id = :id")
    Shop getShopById(long id);

    @Insert
    void insert(Shop shop);

    @Delete
    void delete(Shop shop);

    @Update
    void update(Shop shop);


}
