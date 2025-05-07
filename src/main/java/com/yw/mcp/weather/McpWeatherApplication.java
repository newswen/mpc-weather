package com.yw.mcp.weather;

import com.yw.mcp.weather.domain.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class McpWeatherApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(McpWeatherApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider weatherTool(WeatherService weatherService) {
        return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("weather-mcp server computer success!");
    }
}
