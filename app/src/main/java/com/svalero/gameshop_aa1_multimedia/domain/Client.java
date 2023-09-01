package com.svalero.gameshop_aa1_multimedia.domain;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users", indices = {@Index(value = {"username"}, unique = true)})
public class Client implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String adress;
    private String email;
    private String tlf;
    private String nif;
    private String password;
    private String username;

    public Client(long id, String name, String adress, String email, String tlf, String nif, String password, String username) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.email = email;
        this.tlf = tlf;
        this.nif = nif;
        this.password = password;
        this.username = username;
    }

    public Client(String name, String adress, String email, String tlf, String nif, String password, String username) {
        this.name = name;
        this.adress = adress;
        this.email = email;
        this.tlf = tlf;
        this.nif = nif;
        this.password = password;
        this.username = username;
    }

    public Client() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
