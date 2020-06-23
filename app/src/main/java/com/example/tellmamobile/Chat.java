package com.example.tellmamobile;

import java.util.Date;

public class Chat {
    private Long roomId;
    private String name;
    private Message lastmessages;


    public Chat(Long roomId, String name, Message lastmessages) {
        this.roomId = roomId;
        this.name = name;
        this.lastmessages = lastmessages;
    }


    public Long getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public Message getLastmessages() {
        // TODO: fix it
        lastmessages = new Message((long) 1, "testando ultima mensagem que é muito grande, de forma que passe o limite da tela, acho que já ta bom", roomId, "girardin", new Date());
        return lastmessages;
    }
}
