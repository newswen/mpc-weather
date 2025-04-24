package com.yw.mcp.weather.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class AmapWeatherResponse {
    private String status;
    private String count;
    private String info;
    private String infocode;
    private List<Forecast> forecasts;

    @Data
    public static class Forecast {
        private String city;
        private String adcode;
        private String province;
        private String reporttime;
        private List<Cast> casts;

        @Data
        public static class Cast {
            private String date;
            private String week;
            private String dayweather;
            private String nightweather;
            private String daytemp;
            private String nighttemp;
            private String daywind;
            private String nightwind;
            private String daypower;
            private String nightpower;
            private String daytemp_float;
            private String nighttemp_float;
        }
    }
}
