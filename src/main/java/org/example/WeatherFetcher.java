package org.example;
import java.util.Scanner;

public class WeatherFetcher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String apiKey = "3347fbf7-f523-48c2-bed3-20c56f74e680";

        System.out.print("Введите широту (lat): ");
        double lat = scanner.nextDouble();

        System.out.print("Введите долготу (lon): ");
        double lon = scanner.nextDouble();

        System.out.print("Введите количество дней для расчета средней температуры: ");
        int limit = scanner.nextInt();

        WeatherService weatherService = new WeatherService(apiKey);

        try {
            String jsonResponse = weatherService.getWeatherData(lat, lon);
            System.out.println("Полный ответ от сервиса: " + jsonResponse);

            WeatherData weatherData = weatherService.parseWeatherResponse(jsonResponse, limit);
            System.out.println(weatherData);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка при получении данных о погоде.");
        } finally {
            scanner.close();
        }
    }
}