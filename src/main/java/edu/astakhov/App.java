package edu.astakhov;

import edu.astakhov.DTO.WeatherResponse;
import edu.astakhov.weather.WeatherClient;

public class App
{
    public static void main( String[] args )
    {
        WeatherClient client = new WeatherClient("e4e5572e07661facfd05b8b9294b6a55", WeatherClient.Mode.POLLING);
        WeatherResponse data1 = client.getWeather("London");
        WeatherResponse data2 = client.getWeather("London");
        System.out.println(data1 == data2);
        client.close();
        client.getWeather("London");
    }
}
