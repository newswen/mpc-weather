package com.yw.mcp.weather.domain.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yw.mcp.weather.domain.model.AmapBaseWeatherResponse;
import com.yw.mcp.weather.domain.model.AmapGeoResponse;
import com.yw.mcp.weather.domain.model.AmapWeatherResponse;
import com.yw.mcp.weather.domain.model.JokeResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WeatherService {

    @Value("${gaode.key}")
    private String amapKey;

    private OkHttpClient httpClient;

    public WeatherService() {
        this.httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
    }

    private final String[] days = {"今日", "明日", "后日"};

    @Tool(description = "根据地址查询当日的天气预报")
    public String getWeatherForecast(@ToolParam String address) {
        log.info("查询天气预报地址：{}", address);
        try {
            AmapWeatherResponse geoResponse = this.getWeatherByAddress(address);
            return this.getFormattedWeatherByAddress(geoResponse);
        } catch (Exception e) {
            log.error("查询天气预报地址：{}", address, e);
        }
        return "查询天气预报地址：" + address + "失败";
    }

    public AmapWeatherResponse getWeatherByAddress(String address) throws Exception {
        String geoUrl = "https://restapi.amap.com/v3/geocode/geo?address=" + address + "&key=" + amapKey;
        String geoResponseJson = sendGetRequest(geoUrl);
        AmapGeoResponse geoResponse = JSON.parseObject(geoResponseJson, AmapGeoResponse.class);

        if (geoResponse == null || geoResponse.getGeocodes().isEmpty()) {
            throw new RuntimeException("获取高德天气预报 地址无法解析为 adcode");
        }

        String adcode = geoResponse.getGeocodes().get(0).getAdcode();

        String weatherUrl = "https://restapi.amap.com/v3/weather/weatherInfo?city=" + adcode + "&key=" + amapKey + "&extensions=all";
        String weatherResponseJson = sendGetRequest(weatherUrl);

        String windUrl = "https://restapi.amap.com/v3/weather/weatherInfo?city=" + adcode + "&key=" + amapKey + "&extensions=base";
        String windResponseJson = sendGetRequest(windUrl);

        AmapWeatherResponse weatherResponse = JSON.parseObject(weatherResponseJson, AmapWeatherResponse.class);
        AmapBaseWeatherResponse baseResponse = JSON.parseObject(windResponseJson, AmapBaseWeatherResponse.class);

        weatherResponse.setWindPower(baseResponse.getLives().get(0).getWindpower());
        weatherResponse.setWinddiRection(baseResponse.getLives().get(0).getWinddirection());

        return weatherResponse;
    }

    private String sendGetRequest(String url) throws Exception {
        Request request = new Request.Builder().url(url).get().build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new RuntimeException("请求失败: " + response);
            return response.body().string();
        }
    }

    public String getFormattedWeatherByAddress(AmapWeatherResponse weatherResponse) {
        if (weatherResponse == null || weatherResponse.getForecasts().isEmpty()) {
            throw new RuntimeException("天气预报数据获取失败");
        }

        AmapWeatherResponse.Forecast forecast = weatherResponse.getForecasts().get(0);
        StringBuilder weatherReport = new StringBuilder();

        for (int i = 0; i < 1; i++) {
            AmapWeatherResponse.Forecast.Cast cast = forecast.getCasts().get(i);
            weatherReport.append("\uD83D\uDDD3").append(days[i]).append("【").append(cast.getDate()).append("】\n").append("  \uD83C\uDF24天气：").append(cast.getNightweather()).append(" / ").append(cast.getDayweather()).append("\n").append("  \uD83C\uDF21温度：").append(cast.getNighttemp()).append("°C ~ ").append(cast.getDaytemp()).append("°C\n").append("  \uD83C\uDF00风力：").append(weatherResponse.getWinddiRection()).append("风").append(weatherResponse.getWindPower()).append("级\n");
        }
        return weatherReport.toString();
    }
}
