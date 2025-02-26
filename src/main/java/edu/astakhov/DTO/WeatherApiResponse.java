package edu.astakhov.DTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherApiResponse(
        Coord coord,
        List<Weather> weather,
        String base,
        Main main,
        int visibility,
        Wind wind,
        Clouds clouds,
        long dt,
        Sys sys,
        int timezone,
        long id,
        String name,
        int cod
) {
    public record Coord(
            double lon,
            double lat
    ) {}

    public record Weather(
            int id,
            String main,
            String description,
            String icon
    ) {}

    public record Main(
            double temp,
            @JsonProperty("feels_like") double feelsLike,
            @JsonProperty("temp_min") double tempMin,
            @JsonProperty("temp_max") double tempMax,
            int pressure,
            int humidity,
            @JsonProperty("sea_level") int seaLevel,
            @JsonProperty("grnd_level") int grndLevel
    ) {}

    public record Wind(
            double speed,
            int deg,
            double gust
    ) {}

    public record Clouds(
            int all
    ) {}

    public record Sys(
            int type,
            int id,
            String country,
            long sunrise,
            long sunset
    ) {}
}
