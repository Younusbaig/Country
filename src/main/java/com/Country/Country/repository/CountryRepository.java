package com.Country.Country.repository;

import com.Country.Country.model.Country;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends MongoRepository<Country, String> {

    boolean existsByName(String name);
    Optional<Country> findByName(String name);


}
