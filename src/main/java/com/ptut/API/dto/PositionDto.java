package com.ptut.API.dto;

import java.io.Serializable;

public class PositionDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private double latitude;
    private double longitude;

    public PositionDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PositionDto(double[] pos) {
        this.latitude = pos[0];
        this.longitude = pos[1];
    }

    public double getLat() {
        return latitude;
    }

    public double getLong() {
        return longitude;
    }
}
