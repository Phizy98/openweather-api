package edu.astakhov.weather;

import edu.astakhov.DTO.WeatherResponse;
import edu.astakhov.exception.WeatherApiException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A service responsible for periodically polling the weather API to update cached weather data.
 * The service fetches weather data for the cities stored in the cache at a fixed interval.
 */
public class PollingService {
    private final WeatherService apiService;
    private final WeatherDataCache cache;
    private final String token;
    private ScheduledExecutorService scheduler;
    private final long POLLING_INTERVAL = 10;  // Polling interval in minutes

    /**
     * Creates a new instance of PollingService.
     *
     * @param apiService the WeatherService instance used to fetch weather data
     * @param cache the WeatherDataCache instance used to store weather data
     * @param token the API token used for authentication with the weather service
     */
    public PollingService(WeatherService apiService, WeatherDataCache cache, String token) {
        this.apiService = apiService;
        this.cache = cache;
        this.token = token;
    }

    /**
     * Starts the polling service. The service will periodically fetch weather data
     * for all stored cities in the cache and update the cache with the latest data.
     * The polling occurs at a fixed rate defined by {@link #POLLING_INTERVAL}.
     */
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

    /**
     * Stops the polling service. If the service is currently running, it will stop
     * the scheduler and await termination.
     */
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

