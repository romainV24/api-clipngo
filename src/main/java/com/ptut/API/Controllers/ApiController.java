package com.ptut.API.Controllers;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.ptut.API.entities.PositionEntity;
import com.ptut.API.entities.SignalementEntity;
import com.ptut.API.entities.TypeEntity;
import com.ptut.API.repositories.IPositionRepository;
import com.ptut.API.repositories.ISignalementRepository;
import com.ptut.API.repositories.ITypeRepository;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api")
class ApiController {

    @Autowired
    private IPositionRepository positionRepository;
    @Autowired
    private ISignalementRepository signalementRepository;
    @Autowired
    private ITypeRepository typeRepository;

    @GetMapping(value ="/signalements", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSignalements(@RequestParam(required=false) Double[] posmax, @RequestParam(required=false) Double[] posmin) {
        if (posmax != null && posmin != null ) {
            // Logger.getLogger(ApiController.class.getName()).info("api/signalement");
            Iterable<SignalementEntity> liste = signalementRepository.findAll();
            PositionEntity min = new PositionEntity(posmin[0], posmin[1]);
            PositionEntity max = new PositionEntity(posmax[0], posmax[1]);
            System.out.println(min);
            Iterator<SignalementEntity> it = liste.iterator();
            while (it.hasNext()) {
                if (!it.next().getPosition().compareTo(min, max)) {
                    it.remove();
                }
            }
            System.out.println(min + " - " + max);
            return ResponseEntity.ok().body(liste);
        } else {
            Iterable<SignalementEntity> liste = signalementRepository.findAll();
            return ResponseEntity.ok().body(liste);
        }
    }
    @PostMapping(value ="/signalements", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postSignalements(@RequestBody JSONObject position) {
        LinkedHashMap<String, Double> tab = (LinkedHashMap<String, Double>) position.get("pos_min");
        PositionEntity min = new PositionEntity(tab.get("latitude"), tab.get("longitude"));
        LinkedHashMap<String, Double> tab2 = (LinkedHashMap<String, Double>) position.get("pos_max");
        PositionEntity max = new PositionEntity(tab2.get("latitude"), tab2.get("longitude"));
        Iterable<SignalementEntity> liste2 = signalementRepository.findAll();
        Iterator<SignalementEntity> it = liste2.iterator();
        while (it.hasNext()){
            if (!it.next().getPosition().compareTo(min,max))
            {
                it.remove();
            }
        }
        System.out.println(min+" - "+max);
        return ResponseEntity.ok().body(liste2);
    }
    @PostMapping(value = "/new_signalement",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    //error parsing
    public ResponseEntity<?> addSignalements(@RequestBody JSONObject signalement) {
        SignalementEntity sign;
        try {
        String date = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            date = mapper.writeValueAsString(signalement.get("dhEmissions"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("'yyyy-MM-dd HH:mm:ss'");
        LocalDateTime dh = LocalDateTime.parse(date,format);
        LinkedHashMap<String, Double> tab = (LinkedHashMap<String, Double>) signalement.get("position");
        PositionEntity pos = positionRepository.save(new PositionEntity(tab.get("Latitude"), tab.get("Longitude")));
        Integer idType = (Integer) signalement.get("type");
        Optional<TypeEntity> type = typeRepository.findById(Long.valueOf(idType));
        sign = signalementRepository.save(new SignalementEntity(dh, type.get(), pos));
        return ResponseEntity.ok().body(sign);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(signalement.get("dhEmissions"));
        }
    }
        @PostMapping(value = "/notify_signalement",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSignalements(@RequestBody JSONObject signalement) throws Exception{
            Integer id = (Integer) signalement.get("id");
            try {
                Optional<SignalementEntity> sign = signalementRepository.findById(Long.valueOf(id));
                if (sign.isPresent()){
                    sign.get().setDhEmissions(LocalDateTime.now());
                    sign.get().setNbAnnulations((Integer) signalement.get("nbAnnulations"));
                    sign.get().setNbValidations((Integer) signalement.get("nbValidations"));
                    Integer typeid = (Integer) signalement.get("type");
                    Optional<TypeEntity> type = typeRepository.findById(Long.valueOf(typeid));
                    type.ifPresent(typeEntity -> sign.get().setType(typeEntity));
                    signalementRepository.save(sign.get());
                    return ResponseEntity.ok().body(signalementRepository.findById(sign.get().getID()));
                } else { throw new Exception("Signalement inconnu");}
            } catch (Exception e){
                System.out.println(e.getMessage());
                return ResponseEntity.badRequest().body(e.getMessage());
            }
    }
}