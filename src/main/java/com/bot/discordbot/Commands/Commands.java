package com.bot.discordbot.Commands;

import com.bot.discordbot.Giphy.Giphy;
import com.bot.discordbot.DiscordBotApplication;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.awt.*;
import java.util.Random;

public class Commands extends ListenerAdapter {

    Giphy giphy = new Giphy("oy15EodI1LP4cdPVAJ6RvPBVrae3aSOr");


    public MessageEmbed creditEmbed() {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Creator");
        embedBuilder.addField("pascalyazid#2212", "Creator of the bot", false);
        embedBuilder.setColor(Color.blue);
        embedBuilder.build();
        return embedBuilder.build();
    }


    public String concatSearch() {
        String str1 = "https://api.giphy.com/v1/gifs/search?api_key=oy15EodI1LP4cdPVAJ6RvPBVrae3aSOr&q=";
        String str3 = "&limit=25&offset=0&rating=g&lang=en";
        String concated = str1.concat(str3);
        return concated;
    }


    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        String messageSent = event.getMessage().getContentRaw();
        String name = event.getAuthor().getName();
        Region region = event.getGuild().getRegion();
        Random rand = new Random();
        int number1 = rand.nextInt(8);
        int number = rand.nextInt(100);



        if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "hey")) {
            if (!event.getAuthor().isBot()) {
                event.getChannel().sendMessage("Hey " + name + "!").queue();
            }
        }


        if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "420")) {
            if (!event.getAuthor().isBot()) {
                event.getChannel().sendMessage("https://media0.giphy.com/media/QoexrOUafbBNGYDS1Y/giphy.gif").queue();
            }
        }
        if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "gif")) {
            event.getChannel().sendMessage(giphy.getRandomGif()).queue();
        }
        if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "trendingGif")) {
            event.getChannel().sendMessage(giphy.searchTrending(true)).queue();
        }
        if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "searchGif")) {
            try {
                String searched = args[1];
                event.getChannel().sendMessage(giphy.searchGif(searched, true)).queue();
            } catch (Exception e) {
                event.getChannel().sendMessage("Could not find any Giphys").queue();
            }
        }

        if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "creator")) {
            event.getChannel().sendMessage(creditEmbed()).queue();
        }
        

        if(!event.getAuthor().isBot()) {
            if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "is")) {


                if (number < 50) {
                    event.getChannel().sendMessage(event.getAuthor().getName() + ": No").queue();
                }
                if (number > 50) {
                    event.getChannel().sendMessage(event.getAuthor().getName() + ": Yes").queue();
                }
            }
        }
    }


    public static MessageEmbed SongEmbed(String id, String title, String url) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Adding to queue");
        embedBuilder.addField(title, "", false);
        embedBuilder.setColor(Color.blue);
        embedBuilder.setImage("https://i3.ytimg.com/vi/" + id + "/maxresdefault.jpg");
        embedBuilder.build();
        return embedBuilder.build();
    }

}