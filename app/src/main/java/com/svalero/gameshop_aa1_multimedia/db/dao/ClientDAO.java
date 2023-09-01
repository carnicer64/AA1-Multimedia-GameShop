package com.svalero.gameshop_aa1_multimedia.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.gameshop_aa1_multimedia.domain.Client;

import java.util.List;

@Dao
public interface ClientDAO {
    @Query("SELECT * FROM users")
    List<Client> getAll();

    @Query("SELECT * FROM users WHERE id = :id")
    Client getClientById(long id);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    Client login(String username, String password);

    @Insert
    void insert(Client client);

    @Delete
    void delete(Client client);

    @Update
    void update(Client client);


}
