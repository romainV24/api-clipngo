package com.ptut.API.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "position")
public class PositionEntity implements Serializable {
    public PositionEntity(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PositionEntity() {}

    public void main(Double latitude, Double longitude){
        PositionEntity position = new PositionEntity();

    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(name = "Latitude")
    @JsonProperty("Latitude")
    private double latitude;

    @Column(name = "Longitude")
    @JsonProperty("Longitude")
    private double longitude;

    @OneToOne(mappedBy="position")
    private SignalementEntity signalement;

    public Long getId(){
        return this.id;
    }
    @Override
    public String toString() {
        return "[ "+this.latitude+" - "+this.longitude+" ]";
    }
    public Boolean compareTo(PositionEntity min, PositionEntity max ){
        if (min.latitude < max.latitude){
            if (this.latitude > min.latitude && this.latitude < max.latitude){
                if (min.longitude<max.longitude){
                    if (this.longitude > min.longitude && this.longitude < max.longitude)
                    {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    if (this.longitude > max.longitude && this.longitude < min.longitude)
                    {
                        return true;
                    } else {
                        return false;
                    }
                }

            }
        }
        if (min.latitude>max.latitude){
            if (this.latitude > min.latitude && this.latitude < max.latitude){
                if (min.longitude<max.longitude){
                    if (this.longitude > min.longitude && this.longitude < max.longitude)
                    {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    if (this.longitude > max.longitude && this.longitude < min.longitude)
                    {
                        return true;
                    } else {
                        return false;
                    }
                }

            }else {

                return false;
            }
        }
         else {
            return false;
        }
    }
}
