package com.example.tellmamobile;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    private Long id;
    private String text;
    private Long chat;
    private String username;
    private Date date;

    public Message(Long id, String text, Long chat, String username, Date date) {
        this.id = id;
        this.text = text;
        this.chat = chat;
        this.username = username;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Long getChat() {
        return chat;
    }

    public String getUsername() {
        return username;
    }

    public Date getDate() {
        return date;
    }

    public String getFormattedDate(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");
        return format.format(date);
    }
}
