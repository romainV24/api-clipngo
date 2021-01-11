package com.ptut.API.repositories;

import com.ptut.API.entities.PositionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPositionRepository extends CrudRepository<PositionEntity, Long> {

}
