package com.ptut.API.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ptut.API.LocalDateDeserializer;
import com.ptut.API.LocalDateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name="signalement")
public class SignalementEntity implements Serializable {

    public SignalementEntity(){}
    public SignalementEntity(LocalDateTime date,TypeEntity type, PositionEntity position){
        this.dhEmissions = date;
        this.position = position;
        this.type = type;
        this.nbValidations = 1;
        this.nbAnnulations = 0;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long ID;
    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "dh_emissions")
    @JsonProperty("dhEmissions")
    private LocalDateTime dhEmissions;
    public LocalDateTime getDhEmissions() {
        return dhEmissions;
    }

    public void setDhEmissions(LocalDateTime dhEmissions) {
        this.dhEmissions = dhEmissions;
    }
    @Column(name = "nb_Validations")
    @JsonProperty("nbValidations")
    private int nbValidations;


    public int getNbValidations() {
        return nbValidations;
    }

    public void setNbValidations(int nbValidations) {
        this.nbValidations = nbValidations;
    }


    @Column(name = "nb_Annulations")
    @JsonProperty("nbAnnulations")
    private int nbAnnulations;

    public int getNbAnnulations() {
        return nbAnnulations;
    }

    public void setNbAnnulations(int nbAnnulations) {
        this.nbAnnulations = nbAnnulations;
    }

    @ManyToOne( optional = true)
    @JoinColumn(name = "type_id",nullable = false)
    private TypeEntity type;

    public TypeEntity getType() {
        return type;
    }

    public void setType(TypeEntity type) {
        this.type = type;
    }

    @OneToOne
    @JsonProperty("position")
    private PositionEntity position;

    public PositionEntity getPosition() {
        return position;
    }

    public void setPosition(PositionEntity position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "id:"+ID+"\n date:"+dhEmissions.toString()+"\n nbA:"+nbAnnulations+"\n nbV:"+nbValidations+"\n type:"+type.getLibelle()+"\n position: "+position.getId() ;
    }
}
