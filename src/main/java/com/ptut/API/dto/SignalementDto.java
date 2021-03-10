package com.ptut.API.dto;

import java.io.Serializable;

public class SignalementDto implements Serializable {

    private static final long serialVersionUID = 7972236850037586739L;
    
    private Long type;
    private Double latitude;
    private Double longitude;
    private boolean validation;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Long getType() {
        return type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public boolean isValidation() {
        return validation;
    }

}
