package com.bot.discordbot.Events;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceStreamEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StreamEvent extends ListenerAdapter {

    public void onGuildVoiceStream(GuildVoiceStreamEvent event) {
        //String channel = event.getVoiceState().getChannel().getName()
        if (event.isStream()) {
            if (event.getGuild().getTextChannelsByName("streams", true).isEmpty()) {
                event.getGuild().getDefaultChannel().sendMessage(event.getMember().getEffectiveName() + " is streaming" +
                        " in " + event.getVoiceState().getChannel().getName()).queue();
            } else {
                event.getGuild().getTextChannelsByName("streams", true).get(0).sendMessage(event.getMember().getEffectiveName() + " is streaming" +
                        " in " + event.getVoiceState().getChannel().getName()).queue();

            }
        }


    }
}