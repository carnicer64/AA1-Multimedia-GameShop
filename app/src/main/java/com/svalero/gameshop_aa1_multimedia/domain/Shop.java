package com.svalero.gameshop_aa1_multimedia.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Shop implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String adress;
    private String tlf;
    private String latitude;
    private String longitude;

    public Shop(long id, String name, String adress, String tlf, String latitude, String longitude) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.tlf = tlf;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Shop() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
