package com.yw.mcp.weather.domain.service;

import com.yw.mcp.weather.domain.model.AmapGeoResponse;
import com.yw.mcp.weather.domain.model.AmapWeatherResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${gaode.key}")
    private String amapKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String[] days = {"今日", "明日", "后日"};


    @Tool(description = "根据地址查询天气预报（未来三天）")
    public String getWeatherForecast(String address) {
        // Step 1: 获取adcode
        AmapWeatherResponse geoResponse = this.getWeatherByAddress(address);
        // Step 2: 获取天气预报
        return this.getFormattedWeatherByAddress(geoResponse);
    }

    public AmapWeatherResponse getWeatherByAddress(String address) {
        // Step 1: 获取 adcode
        String geoUrl = "https://restapi.amap.com/v3/geocode/geo?address=" + address + "&key=" + amapKey;
        AmapGeoResponse geoResponse = restTemplate.getForObject(geoUrl, AmapGeoResponse.class);

        if (geoResponse == null || geoResponse.getGeocodes().isEmpty()) {
            throw new RuntimeException("获取高德天气预报 地址无法解析为 adcode");
        }

        //获取地方对应的 adcode号
        String adcode = geoResponse.getGeocodes().get(0).getAdcode();

        // Step 2: 获取天气
        String weatherUrl = "https://restapi.amap.com/v3/weather/weatherInfo?city=" + adcode + "&key=" + amapKey + "&extensions=all";
        AmapWeatherResponse weatherResponse = restTemplate.getForObject(weatherUrl, AmapWeatherResponse.class);

        return weatherResponse;
    }

    public String getFormattedWeatherByAddress(AmapWeatherResponse weatherResponse) {
        if (weatherResponse == null || weatherResponse.getForecasts().isEmpty()) {
            throw new RuntimeException("天气预报数据获取失败");
        }
        // 格式化返回的天气预报
        AmapWeatherResponse.Forecast forecast = weatherResponse.getForecasts().get(0);
        StringBuilder weatherReport = new StringBuilder();
        weatherReport.append("🌈").append(forecast.getCity()).append("未来三天天气预报🌈\n");

        for (int i = 0; i < 3; i++) {
            AmapWeatherResponse.Forecast.Cast cast = forecast.getCasts().get(i);
            weatherReport.append("\uD83D\uDDD3").append(days[i]).append("【").append(cast.getDate()).append("】\n")
                    .append("  \uD83C\uDF24天气：").append(cast.getNightweather()).append(" / ").append(cast.getDayweather()).append("\n")
                    .append("  \uD83C\uDF21温度：").append(cast.getNighttemp()).append("°C ~ ").append(cast.getDaytemp()).append("°C\n")
                    .append("  \uD83C\uDF00风力：").append(cast.getDaywind()).append("风").append(cast.getDaypower()).append("级\n");

            // 如果不是最后一次循环，才添加分隔线
            if (i < 2) { // 或者使用 i != forecast.getCasts().size() - 1 来动态判断
                weatherReport.append("-------------------------------\n");
            }
        }

        return weatherReport.toString();
    }
}
