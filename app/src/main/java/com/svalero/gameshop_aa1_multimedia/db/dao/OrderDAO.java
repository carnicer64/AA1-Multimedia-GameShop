package com.svalero.gameshop_aa1_multimedia.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.gameshop_aa1_multimedia.domain.Order;

import java.util.List;

@Dao
public interface OrderDAO {
    @Query("SELECT * FROM orders")
    List<Order> getAll();

    @Query("SELECT * FROM orders WHERE id = :id")
    Order getOrderById(long id);

    @Query("SELECT * FROM orders WHERE id_client = :idClient")
    Order getOrderByIdClient(long idClient);

    @Insert
    void insert(Order order);

    @Delete
    void delete(Order order);

    @Update
    void update(Order order);


}
