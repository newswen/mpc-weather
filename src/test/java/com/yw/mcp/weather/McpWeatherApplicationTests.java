package com.yw.mcp.weather;

import com.yw.mcp.weather.domain.service.JokeService;
import com.yw.mcp.weather.domain.service.WeatherService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
class McpWeatherApplicationTests {

    @Resource
    private WeatherService weatherService;

    @Resource
    private JokeService jokeService;

    @Test
    void contextLoads() {
        log.info("天气为:{}", weatherService.getWeatherForecast("宁波"));
    }

    public static void main(String[] args) {
        WeatherService weatherService = new WeatherService();

        System.out.println(weatherService.getWeatherForecast("宁波"));
    }

    @Test
    void testJoke() {
        log.info("笑话为:{}", weatherService.getJokes());
    }


}
