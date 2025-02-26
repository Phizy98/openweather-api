package edu.astakhov.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;

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
