package edu.astakhov.weather;

import edu.astakhov.exception.WeatherApiException;
import junit.framework.TestCase;

public class WeatherClientTest extends TestCase {

    public void testCreateClientWithInvalidToken() {
        try {
            WeatherClient client = new WeatherClient("", WeatherClient.Mode.ON_DEMAND);
        } catch (WeatherApiException e) {
            assertEquals(e.getMessage(), "Token cannot be null or empty!");
        }

    }

    public void testCreateClientWithDuplicateToken() {
        try {
            WeatherClient client1 = new WeatherClient("123", WeatherClient.Mode.ON_DEMAND);
            WeatherClient client2 = new WeatherClient("123", WeatherClient.Mode.ON_DEMAND);
        } catch (WeatherApiException e) {
            assertEquals(e.getMessage(), "Client with this API token already exists!");
        }

    }
}