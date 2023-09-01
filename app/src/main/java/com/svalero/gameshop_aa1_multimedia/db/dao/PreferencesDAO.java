package com.svalero.gameshop_aa1_multimedia.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.gameshop_aa1_multimedia.domain.Preferences;

@Dao
public interface PreferencesDAO {
    @Query("SELECT * FROM preferences WHERE id=0")
    Preferences getPreference();

    @Insert
    void insert(Preferences preferences);

    @Update
    void update(Preferences preferences);
}
