package com.bot.discordbot.Commands;


import com.bot.discordbot.Audio.GuildMusicManager;
import com.bot.discordbot.DiscordBotApplication;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import java.util.HashMap;
import java.util.Map;


public class Voice extends ListenerAdapter {
    private Map<Long, GuildMusicManager> musicManagers = new HashMap<>();
    AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    boolean stopped = false;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");


        if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "join")) {
            if (!event.getAuthor().isBot()) {
                VoiceChannel channel = event.getMember().getVoiceState().getChannel();
                if (channel != null) {
                    AudioManager audioManager = event.getGuild().getAudioManager();
                    audioManager.openAudioConnection(channel);
                } else if (channel == null) {
                    event.getChannel().sendMessage("You are not connected to a voice channel").queue();
                }
            }
        }

        if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "dc")) {
            if (!event.getAuthor().isBot()) {
                AudioManager audioManager = event.getGuild().getAudioManager();

                if (audioManager.isConnected()) {
                    audioManager.closeAudioConnection();
                } else if (!audioManager.isConnected()) {
                    event.getChannel().sendMessage("I am currently not connected to a voice channel").queue();
                }

            }
        }

        if(args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "skip")) {
            if (!event.getAuthor().isBot()) {
                AudioSourceManagers.registerRemoteSources(playerManager);
                AudioSourceManagers.registerLocalSource(playerManager);

                skipTrack(event.getChannel());

            }
        }

        if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "play")) {
            AudioSourceManagers.registerRemoteSources(playerManager);
            AudioSourceManagers.registerLocalSource(playerManager);
            if(args[1] != null){
                String identifier = args[1];
                loadAndPlay(event.getChannel(), identifier);
                stopped = false;
            }
            if(stopped == true) {
                continueTrack(event.getChannel());
                stopped = false;
            }

        }

        if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "stop")) {
            if (stopped == false) {
                AudioSourceManagers.registerRemoteSources(playerManager);
                AudioSourceManagers.registerLocalSource(playerManager);

                stop(event.getChannel());
                stopped = true;
            }
        }
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    private void loadAndPlay(final TextChannel channel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Adding to queue " + track.getInfo().title).queue();

                play(channel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();

                play(channel.getGuild(), musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }


    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {
        connectToFirstVoiceChannel(guild.getAudioManager());

        musicManager.scheduler.queue(track);
    }

    private void stop(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        musicManager.scheduler.stop();
        channel.sendMessage("Paused current track.").queue();
    }

    private void continueTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        musicManager.scheduler.continueTrack();
        channel.sendMessage("Continued current track.").queue();

    }

    private void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("Skipped to next track.").queue();
    }

    //public void currentTrack(TextChannel channel, AudioTrack track) {
      //  GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
    //    musicManager.scheduler.onTrackStart(, track);
  //  }

    private static void connectToFirstVoiceChannel(AudioManager audioManager) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                audioManager.openAudioConnection(voiceChannel);
                break;
            }
        }
    }
}

