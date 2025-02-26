package edu.astakhov.DTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * A response from the weather API containing detailed weather data.
 * This record is used to deserialize the JSON response from the API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherApiResponse(
        List<Weather> weather,
        Main main,
        int visibility,
        Wind wind,
        long dt,
        Sys sys,
        int timezone,
        String name
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Weather(
            String main,
            String description
    ) {}
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Main(
            double temp,
            @JsonProperty("feels_like") double feelsLike
    ) {}
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Wind(
            double speed
    ) {}
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Sys(
            long sunrise,
            long sunset
    ) {}
}

