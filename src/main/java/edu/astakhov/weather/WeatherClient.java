package edu.astakhov.weather;

import edu.astakhov.DTO.WeatherResponse;
import edu.astakhov.exception.WeatherApiException;

import java.util.Set;
import java.util.concurrent.*;

public class WeatherClient implements AutoCloseable {
    private final String token;
    private final Mode mode;
    private static final Set<String> tokens = ConcurrentHashMap.newKeySet();
    private static final WeatherService apiService = new WeatherService();
    private final WeatherDataCache cache = new WeatherDataCache();
    private final PollingService pollingService;

    public enum Mode { ON_DEMAND, POLLING }

    public WeatherClient(String token, Mode mode) {
        validateToken(token);
        this.token = token;
        this.mode = mode;

        pollingService = new PollingService(apiService, cache, token);
        if (mode == Mode.POLLING) {
            pollingService.start();
        }
    }

    private void validateToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new WeatherApiException("Token cannot be null or empty!");
        }
        if (!tokens.add(token)) {
            throw new WeatherApiException("Client with this API token already exists!");
        }
    }

    public WeatherResponse getWeather(String city) throws WeatherApiException {
        return cache.get(city)
                .orElseGet(() -> {
                    WeatherResponse response = apiService.fetchWeather(city, token);
                    cache.put(city, response);
                    return response;
                });

    }

    @Override
    public void close() {
        tokens.remove(this.token);
        pollingService.stop();
    }
}
