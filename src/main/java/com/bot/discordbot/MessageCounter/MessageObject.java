package com.bot.discordbot.MessageCounter;

import java.io.Serializable;

public class MessageObject implements Serializable {

    private String username;
    private int messages;
    String serverID;


    public MessageObject( int messages, String username){
        this.username = username;
        this.messages = messages;

    }
    public MessageObject(){}
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMessages() {
        return messages;
    }

    public void setMessages(int messages) {
        this.messages = messages;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }
}