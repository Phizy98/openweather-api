package edu.astakhov.mapper;

import edu.astakhov.DTO.WeatherApiResponse;
import edu.astakhov.DTO.WeatherResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface WeatherMapper {

    WeatherMapper INSTANCE = Mappers.getMapper(WeatherMapper.class);

    @Mapping(source = "weather", target = "weather", qualifiedByName = "mapWeather")
    @Mapping(source = "main.temp", target = "temperature.temp")
    @Mapping(source = "main.feelsLike", target = "temperature.feelsLike")
    @Mapping(source = "sys.sunrise", target = "sys.sunrise")
    @Mapping(source = "sys.sunset", target = "sys.sunset")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "dt", target = "datetime")
    @Mapping(source = "timezone", target = "timezone")
    @Mapping(source = "visibility", target = "visibility")
    @Mapping(source = "wind.speed", target = "wind.speed")
    WeatherResponse map(WeatherApiResponse weatherApiResponse);

    @Named("mapWeather")
    default WeatherResponse.Weather mapWeatherMain(List<WeatherApiResponse.Weather> weatherApiResponse) {
            return new WeatherResponse.Weather(
                    weatherApiResponse.getFirst().main(),
                    weatherApiResponse.getFirst().description()
                    );
    }

}

