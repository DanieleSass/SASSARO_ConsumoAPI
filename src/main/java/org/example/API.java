package org.example;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class API {

    private final String url="https://pokeapi.co/api/v2/";
    private HttpClient client;

    private static API instance = null;

    private API(){
    client = HttpClient.newHttpClient();
    }

    public static API getInstance(){
        if(instance==null){
            instance = new API();
        }
        return  instance;
    }

    public Pokemon getPokemonByName(String nome) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url+"pokemon/"+nome))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() != 200){
            throw new IOException("Errore " + response.statusCode());
        }

        Gson gson = new Gson();
        return gson.fromJson(response.body(), Pokemon.class);

    }
    public Pokemon getPokemonById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url+"pokemon/"+id))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() != 200){
            throw new IOException("Errore " + response.statusCode());
        }

        Gson gson = new Gson();
        return gson.fromJson(response.body(), Pokemon.class);
    }

}

