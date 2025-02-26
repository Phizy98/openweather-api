# Weather SDK

A simple SDK for accessing the OpenWeather API to retrieve current weather data for a given location.

## Features

- Fetch current weather data by city name.
- Caches weather data for up to 10 cities for efficient repeated queries.
- Supports two modes:
    - **On-demand** mode: Updates the weather data only when requested.
    - **Polling** mode: Requests weather updates for all stored cities at regular intervals to ensure zero-latency responses.
- Returns weather data in a structured format.
- Stores data for up to 10 cities at a time with a 10-minute cache expiration.

## Installation

To use the SDK, you must have a Java environment (JDK 11 or higher). Clone this repository and build the project using Maven:

```bash
git clone https://github.com/yourusername/weather-sdk.git
cd weather-sdk
mvn clean install
```
## Usage Example

The SDK supports two operating modes: **On-demand** and **Polling**. Both modes are specified when creating an instance of the client.

#### On-demand Mode

In On-demand mode, the SDK updates the weather data only when requested. Data is fetched and updated each time the `getWeather` method is called.

```java
// Create a client in On-demand mode
// Request current weather for a city
try (WeatherClient client = new WeatherClient("your_token", WeatherClient.Mode.ON_DEMAND)){
    WeatherResponse response = client.getWeather("London");
    System.out.println("Weather in " + response.name() + ": " + response.weather().main());
} catch (RuntimeException e) {
    System.err.println(e.getMessage());
}
```
#### Polling Mode

In Polling mode, the SDK periodically fetches and updates weather data for all stored cities. This ensures near-zero latency when responding to customer requests, as the latest weather information is always available.

```java
// Create a client in Polling mode
// Request current weather for a city
try (WeatherClient client = new WeatherClient("your_token", WeatherClient.Mode.POLLING)){
    WeatherResponse response = client.getWeather("London");
    System.out.println("Weather in " + response.name() + ": " + response.weather().main());
} catch (RuntimeException e) { 
    System.err.println(e.getMessage());
}
```




