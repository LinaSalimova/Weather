package org.example;

public class WeatherData {
    private int currentTemperature;
    private double averageTemperature;

    public WeatherData(int currentTemperature, double averageTemperature) {
        this.currentTemperature = currentTemperature;
        this.averageTemperature = averageTemperature;
    }

    @Override
    public String toString() {
        return "Текущая температура: " + currentTemperature + "°C, Средняя температура: " + averageTemperature + "°C";
    }
}