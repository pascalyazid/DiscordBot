package com.bot.discordbot.Events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Random;

public class GuildMemberJoin extends ListenerAdapter {


    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event){
        String[] messages = {
                "[member] joined. You must construct additional pylons.",
                "Never gonna give [member] up. Never let [member] down!",
                "Hey! Listen! [member] has joined!",
                "Ha! [member] has joined! You activated my trap card!",
                "We've been expecting you, [member].",
                "It's dangerous to go alone, take [member]!",
                "Swoooosh. [member] just landed.",
                "Brace yourselves. [member] just joined the server.",
                "A wild [member] appeared."
        };
        Random rand = new Random();
        int number = rand.nextInt(messages.length);

        //event.getGuild().getDefaultChannel().sendMessage(messages[number].replace("[member]", event.getUser().getName())).queue();
        event.getGuild().getDefaultChannel().sendMessage(messages[number].replace("[member]", event.getMember().getNickname())).queue();
        // Add role
        //event.getGuild().getController().addRolesToMember(event.getMember(), event.getGuild().getRolesByName("Member", true)).complete();
    }

    public MessageEmbed createEmbed() {
        EmbedBuilder info = new EmbedBuilder();
        info.setTitle("Bot info");
        info.addField("Turtles", "I like turtles!", true);
        info.setDescription(" This is some Information");
        info.setColor(Color.blue);
        info.setAuthor("pascalyazid");
        return info.build();
    }
}
