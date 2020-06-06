package com.example.tellmamobile;

public class Message {

    private String idUser;
    private String text;

    public Message(String idUser, String text) {
        this.idUser = idUser;
        this.text = text;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
