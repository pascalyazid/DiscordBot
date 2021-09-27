package com.bot.discordbot.MessageCounter;

import java.awt.*;
import java.net.MalformedURLException;
import java.util.*;

import com.bot.discordbot.Data.JSONHandler;
import com.bot.discordbot.MessageCounter.MessageObject;
import com.bot.discordbot.MessageCounter.MessageServer;

import com.bot.discordbot.DiscordBotApplication;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageMiner extends ListenerAdapter {

    private static Vector<MessageServer> messages;
    ArrayList<MessageObject> users = new ArrayList<>();

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            messages = new Vector<>();
            messages = DiscordBotApplication.getMessages();

            String username = event.getAuthor().getName();
            String serverName = event.getGuild().getName();
            String[] args = event.getMessage().getContentRaw().split("\\s+");


            String mServerName;
            String mUserName;
            int i = 0;
            int j = 0;

            boolean containsServer = false;
            boolean containsUser = false;

            for (i = 0; i < messages.size(); i++) {

                mServerName = messages.get(i).getServerName();
                if (mServerName.contains(serverName)) {
                    containsServer = true;

                    for (j = 0; j < messages.get(i).userList.size(); j++) {

                        mUserName = messages.get(i).userList.get(j).getUsername();
                        if (mUserName.contains(username)) {
                            containsUser = true;
                            messages.get(i).newMessage(j);
                            JSONHandler.saveMessages(messages);
                            break;
                        }
                    }
                    break;
                }
            }

            if (!containsServer) {
                    messages.add(DiscordBotApplication.addServer(serverName));
                containsServer = true;
                messages.get(i).addUserList();
                messages.get(i).addUser(username);
                JSONHandler.saveMessages(messages);
                containsUser = true;
            }
            if (!containsUser) {
                messages.get(i).addUser(username);
                messages.get(i).userList.get(j).setServerID(serverName);
                JSONHandler.saveMessages(messages);
            }

            if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "mcount")) {
                if (!event.getAuthor().isBot()) {
                    int mCount = messages.get(i).userList.get(j).getMessages();
                    event.getChannel().sendMessage("Messages: " + mCount).queue();
                }
            }

            if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "leaderboard")) {
                if (!event.getAuthor().isBot()) {


                    for (int k = 0; k < messages.get(i).userList.size(); k++) {
                        users.add(messages.get(i).userList.get(k));
                    }

                    users.sort(Comparator.comparing(MessageObject::getMessages));

                    for (int k = 0; k > users.size(); k--) {
                        //System.out.println(users.get(k).getMessages() + " : " + users.get(k).getUsername());
                    }
                    event.getChannel().sendMessage(leaderEmbed()).queue();
                    users.clear();
                }
            }
        }
    }

    public MessageEmbed leaderEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Leaderboard");

        if(users.size() >= 1){embedBuilder.addField("1. " + users.get(users.size() -1).getUsername() , users.get(users.size() -1).getMessages() + " Messages", false);}
        if(users.size() >= 2){embedBuilder.addField("2. " + users.get(users.size() -2).getUsername() , users.get(users.size() -2).getMessages() + " Messages", false);}
        if(users.size() >= 3){embedBuilder.addField("3. " + users.get(users.size() -3).getUsername() , users.get(users.size() -3).getMessages() + " Messages", false);}
        if(users.size() >= 4){embedBuilder.addField("4. " + users.get(users.size() -4).getUsername() , users.get(users.size() -4).getMessages() + " Messages", false);}
        if(users.size() >= 5){ embedBuilder.addField("5. " + users.get(users.size() -5).getUsername() , users.get(users.size() -5).getMessages() + " Messages", false);}
        embedBuilder.setColor(Color.blue);
        embedBuilder.build();
        return embedBuilder.build();
    }
}
