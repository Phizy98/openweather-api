package edu.astakhov.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.astakhov.DTO.WeatherApiResponse;
import edu.astakhov.DTO.WeatherResponse;
import edu.astakhov.exception.WeatherApiException;
import edu.astakhov.mapper.WeatherMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class WeatherService {
    private static final String URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherResponse fetchWeather(String city, String token) throws WeatherApiException {
        try {
            String url = String.format("%s?q=%s&appid=%s", URL, city, token);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new WeatherApiException(String.format(
                        "API request failed with status code %s\n Response: %s",
                        response.statusCode(),
                        response.body()
                ));
            }
            WeatherResponse data = WeatherMapper.INSTANCE.map(objectMapper.readValue(response.body(), WeatherApiResponse.class));
            System.out.println(objectMapper.writeValueAsString(data));
            return data;
        } catch (Exception e) {
            throw new WeatherApiException("Failed to fetch weather data: " + e.getMessage());
        }
    }
}

