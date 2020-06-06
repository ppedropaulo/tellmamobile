package com.example.tellmamobile;

public class Chat {
    private Number roomId;
    private String name;


    public Chat(Number roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }


    public Number getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }
}
