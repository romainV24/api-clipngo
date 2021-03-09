package com.ptut.API.repositories;

import com.ptut.API.entities.PositionEntity;
import com.ptut.API.entities.SignalementEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISignalementRepository extends CrudRepository<SignalementEntity, Long>{
    @Query(
            value = "SELECT * FROM signalement INNER JOIN position ON position.id = signalement.position_id\n WHERE latitude >= :min.latitude AND latitude <= :max.latitude  AND longitude >= :max.latitude AND longitude <= min.longitude ;",
            nativeQuery = true)
    List<SignalementEntity> findByZone(@Param("min") PositionEntity posmin, @Param("max") Posi);
}
