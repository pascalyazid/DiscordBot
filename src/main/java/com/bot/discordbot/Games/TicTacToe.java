package com.bot.discordbot.Games;

import com.bot.discordbot.DiscordBotApplication;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;


public class TicTacToe extends ListenerAdapter {
    public String[][] board = new String[3][];
    public boolean gameStarted = false;

    public TicTacToe() {
        gameStarted = false;
    }

    public int playTurn = 1;


    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "end")) {
            endGame();
            event.getChannel().sendMessage("The game has ended").queue();
        } else {
            if (args[0].equalsIgnoreCase(DiscordBotApplication.prefix + "ttt")) {
                if (gameStarted) {
                    event.getChannel().sendMessage(turn(args[1])).queue();
                } else {
                    event.getChannel().sendMessage(tttEmbed()).queue();
                    event.getChannel().sendMessage(startGame()).queue();
                }


            }
        }
    }

    public String startGame() {
        playTurn = 1;
        gameStarted = true;
        setUpBoard();
        return "The game has begun!\n" + getBoard();
    }

    String[] turnPos = new String[]{
            "top¦left¦0¦0",
            "top¦mid¦0¦1",
            "top¦right¦0¦2",
            "mid¦left¦1¦0",
            "mid¦right¦1¦2",
            "bot¦left¦2¦0",
            "bot¦mid¦2¦1",
            "bot¦right¦2¦2",
            "mid¦mid¦1¦1"

    };

    public void setUpBoard() {
        board = new String[][]{{"", "", ""}, {"", "", ""}, {"", "", ""}};
    }

    public void endGame() {
        gameStarted = false;
    }

    public String turn(String turn) {
        if (gameStarted) {
            for (int i = 0; i < turnPos.length; i++) {
                String[] temp = turnPos[i].split("¦");
                if (turn.contains(temp[0]) && turn.contains(temp[1])) {
                    if (board[Integer.parseInt(temp[2])][Integer.parseInt(temp[3])] == "") {
                        String symbol = (playTurn == 1) ? "1" : "2"; // \uD83D\uDFE6" ❌ 🔴
                        board[Integer.parseInt(temp[2])][Integer.parseInt(temp[3])] = symbol;


                        int playertemp = playTurn;
                        if (playTurn == 1) {
                            playTurn = 2;
                        } else {
                            playTurn = 1;
                        }
                        int result = checkBoard();
                        if (result != -1) {
                            endGame();
                            switch (result) {
                                case 0:
                                    return "The game was a tie.\n" + getBoard();
                                case 1:
                                    return ":tada: Player 1 has won the game!\n" + getBoard();
                                case 2:
                                    return ":tada: Player 2 has won the game!\n" + getBoard();
                            }
                        }
                        return "Player " + playertemp + " has played. Player " + playTurn + ", please play!\n" + getBoard();
                    } else {
                        return "This turn was already played!";
                    }
                }
            }
            return "There was an error reading your turn.\nPlease use top:left, top:mid, top:right or mid:left, mid:mid, mid:right or bot:left, bot:mid, bot:right!";
        } else {
            return "Please start the game first!";
        }
    }

    String[] winStates = {
            //Horizontals
            "0¦0¦0¦1¦0¦2",
            "1¦0¦1¦1¦1¦2",
            "2¦0¦2¦1¦2¦2",
            //Verticals
            "0¦0¦1¦0¦2¦0",
            "0¦1¦1¦1¦2¦1",
            "0¦2¦1¦2¦2¦2",
            //Diagonals
            "0¦0¦1¦1¦2¦2",
            "0¦2¦1¦1¦2¦0"
    };

    //Returns winner (-1 not yet 0 tie 1/2 player 1/2
    public int checkBoard() {
        boolean isTie = true;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == "") {
                    isTie = false;
                }
            }
        }
        if (isTie) {
            return 0;
        }


        for (int i = 0; i < winStates.length; i++) {
            String[] temp = winStates[i].split("¦");
            if (board[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])] == board[Integer.parseInt(temp[2])][Integer.parseInt(temp[3])]) {
                if (board[Integer.parseInt(temp[2])][Integer.parseInt(temp[3])] == board[Integer.parseInt(temp[4])][Integer.parseInt(temp[5])]) {
                    if (board[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])] != "") {
                        if (board[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])] == "1") {
                            return 1;
                        } else {
                            return 2;
                        }
                    }
                }
            }
        }
        return -1;
    }

    public String getBoard() {
        String result = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == "1") {
                    result += "❌";
                } else if (board[i][j] == "2") {
                    result += "\uD83D\uDD34";
                } else {
                    result += "\uD83D\uDFE6";
                }
            }
            result += "\n";
        }
        return result;
    }

    public MessageEmbed tttEmbed(){
        EmbedBuilder tttEmbed = new EmbedBuilder();
        tttEmbed.setTitle("Commands Tic Tac Toe");
        tttEmbed.setColor(Color.blue);
        tttEmbed.addField("!ttt", "Start a new Game", false);
        tttEmbed.addField("!end", "reset the ongoing game", false);
        tttEmbed.addField("top:left", "", false);
        tttEmbed.addField("top:mid", "", false);
        tttEmbed.addField("top:right", "", false);
        tttEmbed.addField("mid:left", "", false);
        tttEmbed.addField("mid:mid", "", false);
        tttEmbed.addField("mid:right", "", false);
        tttEmbed.addField("bot:left", "", false);
        tttEmbed.addField("bot:mid", "", false);
        tttEmbed.addField("bot:right", "", false);
        tttEmbed.addField("Example:", "!ttt bot:left", false);
        tttEmbed.build();
        return tttEmbed.build();




    }
}