package org.example;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherService {
    private static final String API_URL = "https://api.weather.yandex.ru/v2/forecast";
    private String apiKey;

    public WeatherService(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getWeatherData(double lat, double lon) throws Exception {
        String urlString = API_URL + "?lat=" + lat + "&lon=" + lon;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Yandex-API-Key", apiKey);
            connection.setConnectTimeout(5000); // Установка таймаута на соединение
            connection.setReadTimeout(5000); // Установка таймаута на чтение

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("HTTP error code: " + responseCode);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } finally {
            if (connection != null) {
                connection.disconnect(); // Закрываем соединение
            }
        }
    }

    public WeatherData parseWeatherResponse(String jsonResponse, int limit) {
        JSONObject jsonObject = new JSONObject(jsonResponse);

        // Получаем текущую температуру
        int currentTemp = jsonObject.getJSONObject("fact").getInt("temp");

        // Вычисляем среднюю температуру за указанный период
        double totalTemp = 0;
        int count = 0;

        JSONArray forecasts = jsonObject.getJSONArray("forecasts");
        for (int i = 0; i < Math.min(limit, forecasts.length()); i++) {
            JSONObject forecast = forecasts.getJSONObject(i);
            totalTemp += forecast.getJSONObject("parts").getJSONObject("day").getInt("temp_avg");
            count++;
        }

        double averageTemp = count > 0 ? totalTemp / count : 0;

        return new WeatherData(currentTemp, averageTemp);
    }
}