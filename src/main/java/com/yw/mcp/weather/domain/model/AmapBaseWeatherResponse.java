package com.yw.mcp.weather.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class AmapBaseWeatherResponse {

    private String status;
    private String count;
    private String info;
    private String infocode;
    private List<WeatherLive> lives;

    @Data
    public static class WeatherLive {
        private String province;
        private String city;
        private String adcode;
        private String weather;
        private String temperature;
        private String winddirection;
        private String windpower;
        private String humidity;
        private String reporttime;
        private String temperatureFloat;
        private String humidityFloat;

    }

}
