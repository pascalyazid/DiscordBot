package com.bot.discordbot.Giphy;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Author: Elia Schenker
 */
public class Giphy {
    private String api_key;

    /**
     * Basic Constructor
     * @param api_key Your API_Key
     */
    public Giphy(String api_key) {
        this.api_key = api_key;
    }

    /**
     * Returns a random gif
     * @return The url of the gif
     */
    public String getRandomGif() {
        try {
            URL url = new URL("https://api.giphy.com/v1/gifs/random?api_key=" + api_key + "&tag=&rating=g");
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String result = "";
            String line;
            while((line = br.readLine()) != null) {
                result +=line;
            }



            int index = result.indexOf("image_url") + 12;
            int counter = index;
            String finished = "";
            while(result.charAt(counter) != '\"') {
                finished+=result.charAt(counter);
                counter++;
            }
            return finished.replace("\\", "");
        }catch(IOException e) {
            System.out.println("There was an error while downloading your gif: " + e);
        }
        return "";
    }

    /**
     * Encodes a string to a url.
     * Example:
     * Test String -> Test%20String
     * @param value The string that should be encoded
     * @return The encoded string
     */
    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    /**
     * Returns a gif according to a search query
     * @param searchParameter The search keyword
     * @param returnRandom Should the function return the first result or a random result
     * @return The returned gif url
     */
    public String searchGif(String searchParameter, boolean returnRandom) {
        try {
            URL url = new URL("https://api.giphy.com/v1/gifs/search?api_key=" + api_key + "&q=" + encodeValue(searchParameter) + "&limit=25&offset=0&rating=g&lang=en");
            return downloadAndProcessMultipleGifs(url, returnRandom);
        }catch (MalformedURLException e) {
            System.out.println("There was an error while downloading your gif: " + e);
        }
        return "";
    }

    /**
     * Returns a trending gif
     * @param returnRandom Should the first gif be returned or a random trending gif
     * @return The gif url
     */
    public String searchTrending(boolean returnRandom) {
        try {
            URL url = new URL("https://api.giphy.com/v1/gifs/trending?api_key=" + api_key + "&limit=25&rating=g");
            return downloadAndProcessMultipleGifs(url, returnRandom);
        }catch(MalformedURLException e) {
            System.out.println("There was an error while downloading your gif: " + e);
        }
        return "";

    }

    /**
     * Used for downloading and processing multiple gifs
     * @param url the api access url
     * @param returnRandom should the first gif or a random gif be returned
     * @return the gif url
     */
    private String downloadAndProcessMultipleGifs(URL url, boolean returnRandom) {
        try {
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String result = "";
            String line;
            while((line = br.readLine()) != null) {
                result +=line;
            }

            int[] indexes = searchReturnIndexes(result, "\"url");
            String finished;
            int counter2 = 0;
            while(true) {
                int index = -1;
                if (returnRandom) {
                    index = indexes[0 + (int) (Math.random() * ((indexes.length - 0) + 1))] + 7;
                } else {
                    index = indexes[counter2] + 7;
                    counter2++;
                }
                int counter = index;
                finished = "";
                while (result.charAt(counter) != '\"') {
                    finished += result.charAt(counter);
                    counter++;
                }
                if(finished.contains("giphy.gif")) {
                    break;
                }
            }
            return finished.replace("\\", "");
        }catch(IOException e) {
            System.out.println("There was an error while downloading your gif: " + e);
        }
        return "";
    }

    private String convertToFullResGif() {
        return "";
    }


    /**
     * Improved indexOf Function which returns multiple matches
     * @param str the text that should be searched
     * @param searchMatch the string that should be searched in the text
     * @return an array out of index-occurences
     */
    private int[] searchReturnIndexes(String str, String searchMatch) {
        ArrayList<Integer> matches = new ArrayList<Integer>();
        int i = str.indexOf(searchMatch);
        while(i >= 0) {
            matches.add(i);
            i = str.indexOf(searchMatch, i+1);
        }
        int[] result = new int[matches.size()];
        for(int j = 0;j<result.length;j++) {
            result[j] = matches.get(j);
        }
        return result;
    }
}