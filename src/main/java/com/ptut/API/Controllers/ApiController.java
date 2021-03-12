package com.ptut.API.Controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.text.Position;

import com.ptut.API.dto.PositionDto;
import com.ptut.API.dto.SignalementDto;
import com.ptut.API.entities.SignalementEntity;
import com.ptut.API.entities.TypeEntity;
import com.ptut.API.repositories.ISignalementRepository;
import com.ptut.API.repositories.ITypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
class ApiController {

    @Autowired
    private ISignalementRepository signalementRepository;

    @Autowired
    private ITypeRepository typeRepository;

    /**
     * Récupération des markers suivant ou non des signalements
     * @param posmax [latitude, longitude]
     * @param posmin [latitude, longitude]
     * @return
     */
    @GetMapping(value ="/signalements", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SignalementEntity> getSignalements(@RequestParam(required=false) double[] posmin, 
                                                   @RequestParam(required=false) double[] posmax) {
        List<SignalementEntity> signalements = null;
        if(posmin == null || posmax == null) {
            return (List<SignalementEntity>) signalementRepository.findAll();
        }
        else {
            PositionDto posMin = new PositionDto(posmin);
            PositionDto posMax = new PositionDto(posmax);
            // Detection zone traverssant le meridien ocean pacifique
            if(posMin.getLong() > posMax.getLong()) {
                // Zone Ouest
                signalements = signalementRepository.findAllByZone(posMin.getLat(), posMin.getLong(), posMax.getLat(), 180);
                // Zone Est
                signalements.addAll(signalementRepository.findAllByZone(posMin.getLat(), -180, posMax.getLat(), posMax.getLong()));
            }
            else {
                signalements = signalementRepository.findAllByZone(posMin.getLat(), posMin.getLong(), posMax.getLat(), posMax.getLong());
            }
        }
        return signalements;
    }

    /**
     * Ajoute un signalement ou mise à jours
     * @param signalementDto
     */
    @PostMapping(value = "/signalement", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void postSignalement(@RequestBody SignalementDto signalementDto) {
        SignalementEntity signalement = signalementRepository.findByTypeAndPosition(signalementDto.getType(), signalementDto.getLatitude(), signalementDto.getLongitude());
        if(signalement == null) {
            Optional<TypeEntity> type = typeRepository.findById(signalementDto.getType());
            if(!type.isPresent()) {
                throw new IllegalArgumentException("Le type est incorrect");
            }
            signalement = new SignalementEntity(type.get(), signalementDto.getLatitude(), signalementDto.getLongitude());
        }
        // Si c'est une annulation on decremente le compteur d'annulation et on incremente le compteur de validation
        // sinon on fait l'inverse
        if(signalementDto.isValidation()) {
            signalement.validation();
        }
        else {
            signalement.annulation();
        }
        // On vérifie si le signalement doit être supprimer
        // Suivant les compteurs d'annulation ou de suppression
        // ou bien en verifiant si il a expiré
        if(signalement.suppression()) {
            signalementRepository.delete(signalement);
        }
        else {
            signalementRepository.save(signalement);
        }
    }
}