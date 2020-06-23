package com.example.tellmamobile;

import java.util.Date;

public class Chat {
    private Long roomId;
    private String name;
    private Boolean lastmessage;
    private String text;
    private String username;
    private Date timestamp;


    public Chat(Long roomId, String name, Boolean lastmessage, String text, String username, Date timestamp) {
        this.roomId = roomId;
        this.name = name;
        this.lastmessage = lastmessage;
        this.text = text;
        this.username = username;
        this.timestamp = timestamp;
    }


    public Long getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public Message getLastmessagesInfo() {
        if(!lastmessage){
            return new Message((long) 1, "", roomId, "", null);
        }

        return new Message((long) 1, text, roomId, username, timestamp);
    }

    public Boolean hasLastMessage(){
        return lastmessage;
    }
}
