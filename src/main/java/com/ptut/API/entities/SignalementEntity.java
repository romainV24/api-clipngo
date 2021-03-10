package com.ptut.API.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ptut.API.LocalDateDeserializer;
import com.ptut.API.LocalDateSerializer;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name="signalement")
public class SignalementEntity implements Serializable {

    private static final long serialVersionUID = 3940613599913440629L;
    private static final int EXPIRATION = 2; // Expiration de 24 H

    public SignalementEntity() {}
    public SignalementEntity(TypeEntity type, double latitude, double longitude){
        this.id = 0;
        this.dhEmissions = new Date();
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.nbValidations = 0;
        this.nbAnnulations = 0;
    }

    @Id
    @NonNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "dh_emissions")
    @JsonProperty("dhEmissions")
    private Date dhEmissions;
    @JsonProperty
    private double latitude;
    @JsonProperty
    private double longitude;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    @JsonSerialize
    private TypeEntity type;

    @Column(name = "nb_Validations")
    @JsonProperty("nbValidations")
    private int nbValidations;

    @Column(name = "nb_Annulations")
    @JsonProperty("nbAnnulations")
    private int nbAnnulations;

    @Override
    public String toString() {
        return String.format("{ id:%d, latitude:%d, longitude:%d, nb_validation:%d, nb_annulation:%d }", id, latitude, longitude, nbValidations, nbAnnulations);
    }

    public void validation() {
        this.nbValidations++;
        this.dhEmissions = new Date();
    }

    public void annulation() {
        this.nbAnnulations++;
    }

    public boolean suppression() {
        if(this.nbAnnulations >= this.nbValidations) {
            return true;
        }
        // Verification de l'expiration du signalement
        Calendar duration = Calendar.getInstance();
        duration.setTime(this.dhEmissions);
        duration.add(Calendar.MINUTE, EXPIRATION);
        if(duration.getTime().before(new Date())) {
            return true;
        }
        return false;
    }
}
