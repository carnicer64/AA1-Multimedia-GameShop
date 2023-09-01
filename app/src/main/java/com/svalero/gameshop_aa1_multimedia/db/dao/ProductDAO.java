package com.svalero.gameshop_aa1_multimedia.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.gameshop_aa1_multimedia.domain.Product;

import java.util.List;

@Dao
public interface ProductDAO {
    @Query("SELECT * FROM product")
    List<Product> getAll();

    @Query("SELECT * FROM product WHERE id = :id")
    Product getProductById(long id);

    @Query("SELECT * FROM product WHERE name = :name")
    Product getProductByName(String name);

    @Insert
    void insert(Product product);

    @Delete
    void delete(Product product);

    @Update
    void update(Product product);


}
