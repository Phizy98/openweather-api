package edu.astakhov.weather;

import edu.astakhov.DTO.WeatherResponse;

import java.util.*;

class WeatherDataCache {
    private final Map<String, CachedEntry> cache = new LinkedHashMap<>(10, 1f, true);
    private static final long CACHE_EXPIRATION_TIME = 600000;

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

    public void put(String city, WeatherResponse data) {
        if (cache.size() >= 10) {
            removeOldestEntry();
        }
        cache.put(city, new CachedEntry(data));
    }

    public Optional<WeatherResponse> get(String city) {
        CachedEntry entry = cache.get(city);
        if (entry != null && !entry.isExpired()) {
            return Optional.of(entry.data);
        }
        return Optional.empty();
    }

    private void removeOldestEntry() {
        Iterator<String> it = cache.keySet().iterator();
        if (it.hasNext()) {
            String oldestCity = it.next();
            cache.remove(oldestCity);
        }
    }

    public Set<String> getStoredCities() {
        return cache.keySet();
    }
}

