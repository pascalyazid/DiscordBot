package com.bot.discordbot.MessageCounter;

import com.bot.discordbot.Data.JSONHandler;
import com.bot.discordbot.MessageCounter.MessageObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageServer implements Serializable {
    private int serverID;
    private int maxUsers;
    private String serverName;
    public List<MessageObject> userList = new ArrayList<MessageObject>();


    public MessageServer(String serverName) {
        this.serverID = JSONHandler.readMessages().size();
        this.serverName = serverName;
    }
    public MessageServer(){}

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerID() {
        return serverID;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public void addUser(String username) {
        if (maxUsers < 10000) {
            userList.add(new MessageObject(1, username));
        }

    }

    public void addUserList() {
        userList = new ArrayList<>();
    }

    public void newMessage(int index) {
        userList.get(index).setMessages(userList.get(index).getMessages() + 1);
    }


}