package edu.astakhov.exception;

public class WeatherApiException extends RuntimeException{
    public WeatherApiException(String message) {
        super(message);
    }
}
