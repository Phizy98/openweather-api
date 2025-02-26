package edu.astakhov.weather;

import edu.astakhov.exception.WeatherApiException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherClientTest {

    @Test
    void testTokenValidation() {
        try (WeatherClient client1 = new WeatherClient("token1", WeatherClient.Mode.ON_DEMAND)) {
            assertNotNull(client1);
            assertThrows(WeatherApiException.class, () -> {
                new WeatherClient("token1", WeatherClient.Mode.ON_DEMAND);
            });

            assertThrows(WeatherApiException.class, () -> {
                new WeatherClient("", WeatherClient.Mode.ON_DEMAND);
            });
        }

    }

    @Test
    void testTokenRemovalAfterClose() {
        try (WeatherClient client = new WeatherClient("token1", WeatherClient.Mode.ON_DEMAND)) {
            assertNotNull(client);
            assertTrue(WeatherClient.getTokens().contains("token1"));
        }
        assertFalse(WeatherClient.getTokens().contains("token1"));
    }
}

