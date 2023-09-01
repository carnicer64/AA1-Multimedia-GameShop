package com.svalero.gameshop_aa1_multimedia.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Product implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private double cost; //Precio de coste
    private double sale; //Precio de venta
    private int barCode;
    private String imageURL;

    public Product(long id, String name, double cost, double sale, int barCode, String imageURL) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.sale = sale;
        this.barCode = barCode;
        this.imageURL = imageURL;
    }

    public Product() {
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public int getBarCode() {
        return barCode;
    }

    public void setBarCode(int barCode) {
        this.barCode = barCode;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", sale=" + sale +
                ", barCode=" + barCode +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
