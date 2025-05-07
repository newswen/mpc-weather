package com.yw.mcp.weather;

import com.yw.mcp.weather.domain.service.WeatherService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class McpWeatherApplicationTests {

    @Resource
    private WeatherService weatherService;

    @Test
    void contextLoads() {
        log.info("天气为:{}", weatherService.getWeatherForecast("宁波"));
    }

    public static void main(String[] args) {
        WeatherService weatherService = new WeatherService();

        System.out.println(weatherService.getWeatherForecast("宁波"));
    }

}
