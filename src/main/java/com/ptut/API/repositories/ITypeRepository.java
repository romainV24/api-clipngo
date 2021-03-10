package com.ptut.API.repositories;

import com.ptut.API.entities.TypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITypeRepository extends CrudRepository<TypeEntity, Long> {}
