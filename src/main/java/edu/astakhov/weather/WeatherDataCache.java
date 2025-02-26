package edu.astakhov.weather;

import edu.astakhov.DTO.WeatherResponse;

import java.util.*;

class WeatherDataCache {
    private final Map<String, CachedEntry> cache = new LinkedHashMap<>(10, 0.75f, true);
    private static final long CACHE_EXPIRATION_TIME = 600000;


    static class CachedEntry {
        WeatherResponse data;
        long timestamp;
        CachedEntry(WeatherResponse data) {
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }
    }

    public void put(String city, WeatherResponse data) {
        if (cache.size() >= 10) {
            Iterator<String> it = cache.keySet().iterator();
            if (it.hasNext()) {
                cache.remove(it.next());
            }
        }
        cache.put(city, new CachedEntry(data));
    }

    public boolean isValid(String city) {
        CachedEntry entry = cache.get(city);
        return entry != null && (System.currentTimeMillis() - entry.timestamp < 600000);
    }

    public Optional<WeatherResponse> get(String city) {
        CachedEntry entry = cache.get(city);
        if (entry != null && (System.currentTimeMillis() - entry.timestamp < CACHE_EXPIRATION_TIME)) {
            return Optional.of(entry.data);
        }
        return Optional.empty();
    }


    public Set<String> getStoredCities() {
        return cache.keySet();
    }
}
