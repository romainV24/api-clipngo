package com.ptut.API.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="type")
public class TypeEntity implements Serializable {

    private static final long serialVersionUID = -5725637192747401821L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonProperty("id")
    private long id;
    public long getId() {
        return id;
    }

    @Column(name="Libelle" )
    @JsonProperty("libelle")
    private String libelle;

    public String getLibelle() {
        return libelle;
    }
    
}
