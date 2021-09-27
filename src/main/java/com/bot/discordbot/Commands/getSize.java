package com.bot.discordbot.Commands;


import com.bot.discordbot.DiscordBotApplication;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.Random;


public class getSize extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Random rand = new Random();
        int number = rand.nextInt(8);
        String name = event.getAuthor().getName();


        if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "size")) {

            if (event.getAuthor().getName().contains("pascalyazid") || event.getAuthor().getName().contains("Elia")) {
               int number1 = 13;
                event.getChannel().sendMessage(name + "s" + " pp " + "is " + number1 + " inches long!").queue();
            }
            else {
                event.getChannel().sendMessage(name + "s " + "is " + number + " inches long!");
            }
        }
    }
}