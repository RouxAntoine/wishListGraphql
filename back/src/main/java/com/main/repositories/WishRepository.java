package com.main.repositories;

import com.main.models.Wish;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface WishRepository extends MongoRepository<Wish, String>{

    Optional<Wish> findByNameAndOwner_Id(String name, String ownerId);

    Collection<Wish> findByOwner_Id(String ownerId);

}