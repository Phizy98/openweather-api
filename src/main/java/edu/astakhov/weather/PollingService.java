package edu.astakhov.weather;

import edu.astakhov.DTO.WeatherResponse;
import edu.astakhov.exception.WeatherApiException;

import java.util.concurrent.*;

class PollingService {
    private final WeatherService apiService;
    private final WeatherDataCache cache;
    private final String token;
    private ScheduledExecutorService scheduler;
    private final long POLLING_INTERVAL = 10;

    public PollingService(WeatherService apiService, WeatherDataCache cache, String token) {
        this.apiService = apiService;
        this.cache = cache;
        this.token = token;
    }

    public void start() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                for (String city : cache.getStoredCities()) {
                    WeatherResponse response = apiService.fetchWeather(city, token);
                    cache.put(city, response);
                }
            } catch (WeatherApiException e) {
                e.printStackTrace();
            }
        }, 0, POLLING_INTERVAL, TimeUnit.MINUTES);
    }

    public void stop() {
        if (scheduler != null) {
            scheduler.shutdownNow();
            try {
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
