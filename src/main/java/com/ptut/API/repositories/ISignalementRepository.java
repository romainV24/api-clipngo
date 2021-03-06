package com.ptut.API.repositories;

import java.util.List;
import com.ptut.API.entities.SignalementEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISignalementRepository extends CrudRepository<SignalementEntity, Long>{

    @Query(value = "SELECT dh_emissions, nb_annulations,nb_validations ,type_id,TRUNC(CAST(latitude AS NUMERIC), 4) AS latitude,TRUNC(CAST(longitude AS NUMERIC), 4) AS longitude,ID FROM signalement "
                 + "WHERE latitude >= ?1 AND latitude <= ?3 "
                 + "AND longitude >= ?2 AND longitude <= ?4", nativeQuery = true)
    List<SignalementEntity> findAllByZone(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude);

    @Query(value = "SELECT dh_emissions, nb_annulations,nb_validations ,type_id,TRUNC(CAST(latitude AS NUMERIC), 4) AS latitude,TRUNC(CAST(longitude AS NUMERIC), 4) AS longitude,ID FROM signalement "
            + "WHERE latitude >= ?1 AND latitude <= ?3 "
            + "AND longitude >= ?2 AND longitude <= ?4"
            + "AND \"type_id\" = ?5", nativeQuery = true)
    List<SignalementEntity> findAllByZoneAndType(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude,int type );

}
