package com.ptut.API.repositories;

import java.util.List;
import com.ptut.API.entities.SignalementEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISignalementRepository extends CrudRepository<SignalementEntity, Long>{

    @Query(value = "dh_emissions, nb_annulations,nb_validations ,type_id,TRUNC(CAST(latitude AS NUMERIC), 4) AS latitude,TRUNC(CAST(longitude AS NUMERIC), 4) AS longitude,ID"
                 + "WHERE latitude >= ?1 AND latitude <= ?3 "
                 + "AND longitude >= ?2 AND longitude <= ?4", nativeQuery = true)
    List<SignalementEntity> findAllByZone(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude);

    @Query(value = "SELECT dh_emissions,nb_annulations,nb_validations ,type_id,TRUNC(CAST(latitude AS NUMERIC), 4) AS latitude,TRUNC(CAST(longitude AS NUMERIC), 4) AS longitude,ID FROM signalement "
                 + "WHERE type_id = ?1 AND latitude = ?2 "
                 + "AND longitude = ?3 LIMIT 1", nativeQuery = true)
    SignalementEntity findByTypeAndPosition(long type, double latitude, double longitude);

}
