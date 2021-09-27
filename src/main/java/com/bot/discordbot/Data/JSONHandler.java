package com.bot.discordbot.Data;

import com.bot.discordbot.MessageCounter.MessageServer;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

public class JSONHandler {

    private static final Collection<String> SCOPES =
            Arrays.asList("https://www.googleapis.com/auth/youtube.force-ssl");

    private static final String APPLICATION_NAME = "API code samples";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    public static Vector<MessageServer> readMessages(){

        Vector<MessageServer> map = new Vector<>();

        try {

            URL url = new URL("https://api.npoint.io/7f52a96761f6502449cf");

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

            URL url = new URL ("https://api.npoint.io/7f52a96761f6502449cf");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);



            String jsonInputString = new ObjectMapper().writeValueAsString(map.toArray());
            jsonInputString = "[" + jsonInputString.substring(1, jsonInputString.length() - 1) + "]";



            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
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

    /**
     * Create an authorized Credential object.
     *
     * @return an authorized Credential object.
     */
    public static Credential authorize(final NetHttpTransport httpTransport) throws IOException {
                Credential credential;

            try {
                URL url = new URL("https://api.npoint.io/079935ecc50ebbe20fff");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestProperty("accept", "application/json");

                InputStream in = connection.getInputStream();
                GoogleClientSecrets clientSecrets =
                        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
                // Build flow and trigger user authorization request.
                GoogleAuthorizationCodeFlow flow =
                        new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                                .build();
                Credential credential1 =
                        new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

                credential = credential1;



            } catch (JsonProcessingException e) {
                e.printStackTrace();
                credential = null;
            }

        // Load client secrets.
        return credential;
    }

    public static void search(String identifier) throws IOException, GeneralSecurityException {
        YouTube youtubeService = JSONHandler.getService();

        YouTube.Search.List request = youtubeService.search()
                .list(identifier);
        SearchListResponse response = request.execute();
        System.out.println(response);
    }

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(httpTransport);
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
