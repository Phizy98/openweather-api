package edu.astakhov.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A response from the weather application containing processed weather data.
 * This record represents the data after the application processes the raw API response
 * and organizes it in a more usable format.
 */
public record WeatherResponse(
        Weather weather,
        Temperature temperature,
        int visibility,
        Wind wind,
        long datetime,
        Sys sys,
        int timezone,
        String name
) {
    public record Weather(
            String main,
            String description
    ) {}

    public record Temperature(
            double temp,
            @JsonProperty("feels_like") double feelsLike
    ) {}

    public record Wind(
            double speed
    ) {}

    public record Sys(
            long sunrise,
            long sunset
    ) {}
}
