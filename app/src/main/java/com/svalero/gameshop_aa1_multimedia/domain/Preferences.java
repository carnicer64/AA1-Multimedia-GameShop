package com.svalero.gameshop_aa1_multimedia.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Preferences {
    @PrimaryKey
    private long id;
    private boolean autoLogin;
    private boolean backRegister;
    private boolean autoMarker;
    private String username;
    private String password;

    @Override
    public String toString() {
        return "Preferences{" +
                "id=" + id +
                ", autoLogin=" + autoLogin +
                ", backRegister=" + backRegister +
                ", autoMarker=" + autoMarker +
                '}';
    }

    public Preferences(long id, boolean autoLogin, boolean backRegister, boolean autoMarker, String username, String password) {
        this.id = id;
        this.autoLogin = autoLogin;
        this.backRegister = backRegister;
        this.autoMarker = autoMarker;
        this.username = username;
        this.password = password;
    }

    public Preferences() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public void setBackRegister(boolean backRegister) {
        this.backRegister = backRegister;
    }

    public void setAutoMarker(boolean autoMarker) {
        this.autoMarker = autoMarker;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public boolean isBackRegister() {
        return backRegister;
    }

    public boolean isAutoMarker() {
        return autoMarker;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
