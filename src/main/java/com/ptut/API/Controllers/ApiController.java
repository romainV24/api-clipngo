package com.ptut.API.Controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

// package com.ptut.API.Controllers;

// import com.fasterxml.jackson.core.JsonGenerationException;
// import com.ptut.API.entities.PositionEntity;
// import com.ptut.API.entities.SignalementEntity;
// import com.ptut.API.entities.TypeEntity;
// import com.ptut.API.repositories.IPositionRepository;
// import com.ptut.API.repositories.ISignalementRepository;
// import com.ptut.API.repositories.ITypeRepository;
// import net.minidev.json.JSONArray;
// import net.minidev.json.JSONObject;
// import net.minidev.json.parser.JSONParser;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;


// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.util.*;

// @RestController
// @RequestMapping("/api")
// class ApiController {

//     @Autowired
//     private IPositionRepository positionRepository;
//     @Autowired
//     private ISignalementRepository signalementRepository;
//     @Autowired
//     private ITypeRepository typeRepository;

//     @GetMapping(value ="/signalements", produces = MediaType.APPLICATION_JSON_VALUE)
//     public ResponseEntity<?> getSignalements(@RequestParam(required=false) Double[] posmax, @RequestParam(required=false) Double[] posmin) {
//         if (posmax != null && posmin != null ) {
//             // Logger.getLogger(ApiController.class.getName()).info("api/signalement");
//             Iterable<SignalementEntity> liste = signalementRepository.findAll();
//             PositionEntity min = new PositionEntity(posmin[0], posmin[1]);
//             PositionEntity max = new PositionEntity(posmax[0], posmax[1]);
//             System.out.println(min);
//             Iterator<SignalementEntity> it = liste.iterator();
//             while (it.hasNext()) {
//                 if (!it.next().getPosition().compareTo(min, max)) {
//                     it.remove();
//                 }
//             }
//             System.out.println(min + " - " + max);
//             return ResponseEntity.ok().body(liste);
//         } else {
//             Iterable<SignalementEntity> liste = signalementRepository.findAll();
//             return ResponseEntity.ok().body(liste);
//         }
//     }
//     @PostMapping(value ="/signalements", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//     public ResponseEntity<?> postSignalements(@RequestBody JSONObject position) {
//         LinkedHashMap<String, Double> tab = (LinkedHashMap<String, Double>) position.get("pos_min");
//         PositionEntity min = new PositionEntity(tab.get("latitude"), tab.get("longitude"));
//         LinkedHashMap<String, Double> tab2 = (LinkedHashMap<String, Double>) position.get("pos_max");
//         PositionEntity max = new PositionEntity(tab2.get("latitude"), tab2.get("longitude"));
//         Iterable<SignalementEntity> liste2 = signalementRepository.findAll();
//         Iterator<SignalementEntity> it = liste2.iterator();
//         while (it.hasNext()){
//             if (!it.next().getPosition().compareTo(min,max))
//             {
//                 it.remove();
//             }
//         }
//         System.out.println(min+" - "+max);
//         return ResponseEntity.ok().body(liste2);
//     }
//     @PostMapping(value = "/new_signalement",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//     //error parsing
//     public ResponseEntity<?> addSignalements(@RequestBody JSONObject signalement) {
//         SignalementEntity sign;
//         try {
//         String date = "";
//         ObjectMapper mapper = new ObjectMapper();
//         try {
//             date = mapper.writeValueAsString(signalement.get("dhEmissions"));
//         } catch (JsonProcessingException e) {
//             e.printStackTrace();
//         }
//         DateTimeFormatter format = DateTimeFormatter.ofPattern("'yyyy-MM-dd HH:mm:ss'");
//         LocalDateTime dh = LocalDateTime.parse(date,format);
//         LinkedHashMap<String, Double> tab = (LinkedHashMap<String, Double>) signalement.get("position");
//         PositionEntity pos = positionRepository.save(new PositionEntity(tab.get("Latitude"), tab.get("Longitude")));
//         Integer idType = (Integer) signalement.get("type");
//         Optional<TypeEntity> type = typeRepository.findById(Long.valueOf(idType));
//         sign = signalementRepository.save(new SignalementEntity(dh, type.get(), pos));
//         return ResponseEntity.ok().body(sign);
//         } catch (Exception e){
//             System.out.println(e.getMessage());
//             return ResponseEntity.badRequest().body(signalement.get("dhEmissions"));
//         }
//     }
//         @PostMapping(value = "/notify_signalement",consumes = MediaType.APPLICATION_JSON_VALUE)
//     public ResponseEntity<?> updateSignalements(@RequestBody JSONObject signalement) throws Exception{
//             Integer id = (Integer) signalement.get("id");
//             try {
//                 Optional<SignalementEntity> sign = signalementRepository.findById(Long.valueOf(id));
//                 if (sign.isPresent()){
//                     sign.get().setDhEmissions(LocalDateTime.now());
//                     sign.get().setNbAnnulations((Integer) signalement.get("nbAnnulations"));
//                     sign.get().setNbValidations((Integer) signalement.get("nbValidations"));
//                     Integer typeid = (Integer) signalement.get("type");
//                     Optional<TypeEntity> type = typeRepository.findById(Long.valueOf(typeid));
//                     type.ifPresent(typeEntity -> sign.get().setType(typeEntity));
//                     signalementRepository.save(sign.get());
//                     return ResponseEntity.ok().body(signalementRepository.findById(sign.get().getID()));
//                 } else { throw new Exception("Signalement inconnu");}
//             } catch (Exception e){
//                 System.out.println(e.getMessage());
//                 return ResponseEntity.badRequest().body(e.getMessage());
//             }
//     }
// }

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
        if(posmin == null || posmax == null) {
            return (List<SignalementEntity>) signalementRepository.findAll();
        }
        else {
            return signalementRepository.findAllByZone(posmin[0], posmin[1], posmax[0], posmax[1]);
        }
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