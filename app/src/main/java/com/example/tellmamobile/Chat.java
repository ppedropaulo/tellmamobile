package com.example.tellmamobile;

public class Chat {
    private Long roomId;
    private String name;


    public Chat(Long roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }


    public Long getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }
}
