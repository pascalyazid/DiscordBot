package com.bot.discordbot.Commands;



import com.bot.discordbot.DiscordBotApplication;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;

public class helpCommand extends ListenerAdapter {


    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "help")) {
            event.getChannel().sendMessage(helpEmbed()).queue();
        }
    }

    public MessageEmbed helpEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Commands");
        embedBuilder.addField("!hey" , "Bot responds to your hey", true);
        embedBuilder.addField("!is", "Bot says Yes or No", false);
        embedBuilder.addField("!gif", "sends a random Gif", false);
        embedBuilder.addField("!trendingGif", "sends trending Gifs", true);
        embedBuilder.addField("!searchGif [value]", "searches for Gifs with your value", false);
        embedBuilder.addField("!size", "pp size", false);
        embedBuilder.addField("!ttt", "Tic Tac Toe Game", false);
        embedBuilder.addField("!mCount", "shows how many messages you have sent", false);
        embedBuilder.addField("!leaderboard", "Shows the top 5 users with the highest message score", false);
        embedBuilder.addField("!creator", "Creators of the bot", false);
        embedBuilder.addField("!join", "Bot joins your current voice channel", false);
        embedBuilder.addField("!dc", "Bot disconnects form current voice channel", false);
        embedBuilder.addField("!play [url]", "Play a Youtube video / mp3 file", false);
        embedBuilder.addField("!play [name]", "Search a video on Youtube and play the most relevant one", false);
        embedBuilder.addField("!play", "Continue the current audio", false);
        embedBuilder.addField("!skip", "Skip the current audio", false);
        embedBuilder.addField("!stop", "Stop the current audio", false);
        embedBuilder.setColor(Color.blue);
        embedBuilder.build();
        return embedBuilder.build();
    }
}