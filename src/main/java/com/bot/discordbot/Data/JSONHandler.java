package com.bot.discordbot.Data;

import com.bot.discordbot.MessageCounter.MessageServer;
import com.bot.discordbot.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.json.JSONTokener;
import org.jsoup.nodes.Document;

import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;

public class JSONHandler {

    private static final Collection<String> SCOPES =
            Arrays.asList("https://www.googleapis.com/auth/youtube.force-ssl");


    public static Vector<MessageServer> readMessages(){

        Vector<MessageServer> map = new Vector<>();

        try {

            URL url = new URL("[Your JSON-URL here]");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("accept", "application/json");

            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();

            MessageServer[] messages1 = mapper.readValue(responseStream, MessageServer[].class);
            map.addAll(Arrays.asList(messages1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    public static void saveMessages(Vector<MessageServer> map) {
        try {

            URL url = new URL ("[Your JSON-URL here]");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);



            String jsonInputString = new ObjectMapper().writeValueAsString(map.toArray());
            jsonInputString = "[" + jsonInputString.substring(1, jsonInputString.length() - 1) + "]";



            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  String[] searchVideo(String keyword) throws IOException {
        String[] data = new String[3];
        keyword = keyword.replace(" ", "+");
        URL url = new URL("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=relevance&q=" + keyword + "&[Your API key here]");
        InputStream inputStream = new URL(url.toString()).openStream();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int counter;
            while((counter = reader.read()) != -1) {
                sb.append((char) counter);
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            String idObj = jsonObject.getJSONArray("items").get(0).toString();
            JSONObject jsonObject1 = new JSONObject(idObj);
            String id = jsonObject1.getJSONObject("id").getString("videoId");
            String title = jsonObject1.getJSONObject("snippet").getString("title");
            System.out.println(id);
            String videoURL = "https://www.youtube.com/watch?v=" + id;
            System.out.println(videoURL);
            data[0] = videoURL;
            data[1] = id;
            data[2] = title;
            return data;

        } catch (Exception e) {
            //e.printStackTrace();
            return data;
        }
    }
}
