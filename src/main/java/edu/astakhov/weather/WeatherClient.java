package edu.astakhov.weather;

import edu.astakhov.DTO.WeatherResponse;
import edu.astakhov.exception.WeatherApiException;
import lombok.Getter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WeatherClient is responsible for fetching weather data from the WeatherService and caching it.
 * It supports two modes of operation: ON_DEMAND and POLLING.
 *
 * <p>This client uses an API token for authentication and ensures that only one client can
 * exist per token. It caches the weather data for up to 10 cities and checks if the cached data
 * is still valid (less than 10 minutes old) before making an API call.</p>
 *
 * <p>When in POLLING mode, it will periodically update the weather data. In ON_DEMAND mode,
 * it will only fetch weather data when requested.</p>
 */
public class WeatherClient implements AutoCloseable {
    /**
     * The API token used for authentication.
     */
    private final String token;

    /**
     * The mode of operation for the WeatherClient (either ON_DEMAND or POLLING).
     */
    private final Mode mode;

    /**
     * A set of active tokens to ensure that only one client can exist for each token.
     */
    @Getter
    private static final Set<String> tokens = ConcurrentHashMap.newKeySet();

    /**
     * The service used to fetch weather data from the API.
     */
    private static final WeatherService apiService = new WeatherService();

    /**
     * The cache that stores weather data for up to 10 cities.
     */
    private final WeatherDataCache cache = new WeatherDataCache();

    /**
     * The polling service, used in POLLING mode to periodically update weather data.
     */
    private final PollingService pollingService;

    /**
     * The mode of operation for the WeatherClient.
     */
    public enum Mode { ON_DEMAND, POLLING }

    /**
     * Constructs a WeatherClient with the specified token and mode.
     *
     * @param token the API token for authentication
     * @param mode the mode in which the client should operate (ON_DEMAND or POLLING)
     * @throws WeatherApiException if the token is invalid or already in use
     */
    public WeatherClient(String token, Mode mode) {
        validateToken(token);
        this.token = token;
        this.mode = mode;

        pollingService = new PollingService(apiService, cache, token);
        if (mode == Mode.POLLING) {
            pollingService.start();
        }
    }

    /**
     * Validates the token to ensure it is not null, empty, or already in use.
     *
     * @param token the token to validate
     * @throws WeatherApiException if the token is invalid or already in use
     */
    private void validateToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new WeatherApiException("Token cannot be null or empty!");
        }
        if (!tokens.add(token)) {
            throw new WeatherApiException("Client with this API token already exists!");
        }
    }

    /**
     * Gets the weather data for the specified city. If the data is cached,
     * it is returned directly. Otherwise, a request is made to the WeatherService.
     *
     * @param city the name of the city for which to fetch the weather data
     * @return the weather data for the city
     * @throws WeatherApiException if there is an error fetching the weather data
     */
    public WeatherResponse getWeather(String city) throws WeatherApiException {
        return cache.get(city)
                .orElseGet(() -> {
                    WeatherResponse response = apiService.fetchWeather(city, token);
                    cache.put(city, response);
                    return response;
                });
    }

    /**
     * Closes the WeatherClient, releasing any resources and stopping the polling service.
     * This will also remove the token from the set of active tokens.
     */
    @Override
    public void close() {
        tokens.remove(this.token);
        pollingService.stop();
    }
}

