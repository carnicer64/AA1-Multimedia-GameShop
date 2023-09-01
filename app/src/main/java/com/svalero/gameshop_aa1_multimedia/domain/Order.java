package com.svalero.gameshop_aa1_multimedia.domain;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.svalero.gameshop_aa1_multimedia.util.DateConverter;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "orders", foreignKeys = {
        @ForeignKey(entity = Client.class, parentColumns = "id", childColumns = "id_client", onDelete = CASCADE)
})
public class Order implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @TypeConverters(DateConverter.class)
    public Date pDate;
    @TypeConverters(DateConverter.class)
    public Date dDate;
    private double orderPrice;
    private String note;

    private long id_client;

    public Order(long id, Date pDate, Date dDate, double orderPrice, String note, long id_client) {
        this.id = id;
        this.pDate = pDate;
        this.dDate = dDate;
        this.orderPrice = orderPrice;
        this.note = note;
        this.id_client = id_client;
    }

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getpDate() {
        return pDate;
    }

    public void setpDate(Date pDate) {
        this.pDate = pDate;
    }

    public Date getdDate() {
        return dDate;
    }

    public void setdDate(Date dDate) {
        this.dDate = dDate;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getId_client() {
        return id_client;
    }

    public void setId_client(long id_client) {
        this.id_client = id_client;
    }
}
