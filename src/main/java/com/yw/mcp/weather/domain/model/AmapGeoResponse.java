package com.yw.mcp.weather.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class AmapGeoResponse {
    private String status;
    private String info;
    private String infocode;
    private List<Geocode> geocodes;

    @Data
    public static class Geocode {
        private String formatted_address;
        private String country;
        private String province;
        private String citycode;
        private String city;
        private String district;
        private String adcode;
        private String location;
    }
}
