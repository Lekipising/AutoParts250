package com.autoparts.autoparts.repository;

import com.autoparts.autoparts.classes.Another;

import org.springframework.data.repository.CrudRepository;

public interface AnotherRepository extends CrudRepository<Another, String>{
    Another findByConf(String conf);
}
