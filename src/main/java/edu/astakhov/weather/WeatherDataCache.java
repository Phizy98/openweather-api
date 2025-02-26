package edu.astakhov.weather;

import edu.astakhov.DTO.WeatherResponse;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A class that implements a cache for storing weather data for cities.
 * The cache stores no more than 10 cities, and removes the oldest data when the size exceeds 10.
 * The stored data is considered valid for 10 minutes.
 */
public class WeatherDataCache {
    private final Map<String, CachedEntry> cache = new LinkedHashMap<>(10, 1f, true);
    private static final long CACHE_EXPIRATION_TIME = 600000;

    /**
     * Inner class representing a cached entry with weather data and a timestamp.
     */
    static class CachedEntry {
        WeatherResponse data;
        long timestamp;
        CachedEntry(WeatherResponse data) {
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }
        boolean isExpired() {
            return System.currentTimeMillis() - timestamp >= CACHE_EXPIRATION_TIME;
        }
    }

    /**
     * Adds weather data to the cache for the specified city.
     * If the cache already contains 10 entries, the oldest entry will be removed.
     *
     * @param city the name of the city for which to cache weather data
     * @param data the weather data for the specified city
     */
    public void put(String city, WeatherResponse data) {
        if (cache.size() >= 10) {
            removeOldestEntry();
        }
        cache.put(city, new CachedEntry(data));
    }

    /**
     * Retrieves weather data for the specified city from the cache if it is still valid.
     *
     * @param city the name of the city to retrieve data for
     * @return an {@link Optional} containing the weather data if available and valid, otherwise {@link Optional#empty()}
     */
    public Optional<WeatherResponse> get(String city) {
        CachedEntry entry = cache.get(city);
        if (entry != null && !entry.isExpired()) {
            return Optional.of(entry.data);
        }
        return Optional.empty();
    }

    /**
     * Removes the oldest entry from the cache.
     * This method is called when the cache exceeds the maximum size of 10 entries.
     */
    private void removeOldestEntry() {
        Iterator<String> it = cache.keySet().iterator();
        if (it.hasNext()) {
            String oldestCity = it.next();
            cache.remove(oldestCity);
        }
    }

    /**
     * Retrieves the set of cities whose weather data is currently stored in the cache.
     *
     * @return a {@link Set} of city names stored in the cache
     */
    public Set<String> getStoredCities() {
        return cache.keySet();
    }
}


