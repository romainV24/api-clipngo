package com.ptut.API.repositories;

import com.ptut.API.entities.SignalementEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISignalementRepository extends CrudRepository<SignalementEntity, Long>{

}
