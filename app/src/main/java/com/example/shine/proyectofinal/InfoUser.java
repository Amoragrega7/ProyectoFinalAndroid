package com.example.shine.proyectofinal;


public class InfoUser {
    private String avatar;
    private String username;
    private String password;
    private int bestpoints;
    private int notificationmode;

    public InfoUser(String a, String username, int puntos){
        this.avatar = a;
        this.username = username;
        this.password = "";
        bestpoints = puntos;
        notificationmode = 0;

    }
    InfoUser(){}

    public String getUsername(){
        return username;
    }

    public void setUsername(String s) {
        this.username = s;
    }

    public void setPassword(String s) {
        this.password = s;
    }

    public String getPassword(){
        return password;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getPoints() {
        return bestpoints;
    }
}
