package com.yw.mcp.weather.domain.model;


import lombok.Data;

import java.util.List;

@Data
public class JokeResponse {

    private int code;
    private String msg;
    private List<DataItem> data;

    // Inner class to represent each data item
    @Data
    public static class DataItem {
        private String content;
        private String updateTime;
    }
}