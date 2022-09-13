package com.bot.discordbot;

import com.bot.discordbot.Commands.Commands;
import com.bot.discordbot.Commands.Voice;
import com.bot.discordbot.Commands.getSize;
import com.bot.discordbot.Commands.helpCommand;
import com.bot.discordbot.Data.JSONHandler;
import com.bot.discordbot.Events.GuildMemberJoin;
import com.bot.discordbot.Games.TicTacToe;
import com.bot.discordbot.MessageCounter.MessageMiner;
import com.bot.discordbot.MessageCounter.MessageServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.security.GeneralSecurityException;
import java.util.Vector;


public class DiscordBotApplication extends ListenerAdapter {

    public static String prefix = "!";
    private static Vector<MessageServer> messages;

    public static void main(String[] args) throws GeneralSecurityException, IllegalArgumentException{
        messages = new Vector<>();
        messages = JSONHandler.readMessages();
        JDA jda = JDABuilder.createDefault("[Your DiscordBot-Token here]").build();

        jda.addEventListener(new Commands());
        jda.addEventListener(new getSize());
        jda.addEventListener(new helpCommand());
        jda.addEventListener(new GuildMemberJoin());
        jda.addEventListener(new TicTacToe());
        jda.addEventListener(new MessageMiner());
        jda.addEventListener(new Voice());

    }

    public static MessageServer addServer(String serverName){

        MessageServer server = new MessageServer(serverName);
        return server;
    }

    public static Vector<MessageServer> getMessages() {
        return messages;
    }

}
