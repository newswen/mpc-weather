package com.yw.mcp.weather.domain.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yw.mcp.weather.domain.model.JokeResponse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class JokeService {

    private static final String JOKE_URL = "https://www.mxnzp.com/api/jokes/list/random";
    private static final String JOKE_APP_ID = "pdddpuglpovpip3m";
    private static final String JOKE_APP_SECRET = "GSjp84VsFqZH2Yis7h9sQe4ieOgN5YXD";

    private ObjectMapper objectMapper;

    private OkHttpClient httpClient;

    public JokeService(ObjectMapper objectMapper) {
        this.httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        this.objectMapper = new ObjectMapper();
    }

    @Tool(description = "每日搞笑段子")
    public String getJokes() {
        // Build the request URL with the query parameters
        String url = String.format("%s?app_id=%s&app_secret=%s", JOKE_URL, JOKE_APP_ID, JOKE_APP_SECRET);

        // Create the request
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Send the request and handle the response
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // Parse the response body to a JokeResponse object
                JokeResponse jokeResponse = objectMapper.readValue(response.body().string(), JokeResponse.class);

                // Check if the response contains data
                if (jokeResponse.getCode() == 1 && jokeResponse.getData() != null && !jokeResponse.getData().isEmpty()) {
                    // Get the first joke content and return it
                    return jokeResponse.getData().get(0).getContent();
                } else {
                    log.error("No jokes found or error in response: {}", jokeResponse.getMsg());
                    return "No jokes available.";
                }
            } else {
                log.error("Request failed with status code: {}", response.code());
                return "Failed to fetch jokes.";
            }
        } catch (IOException e) {
            log.error("Error during API call: ", e);
            return "Error fetching jokes.";
        }
    }

}
