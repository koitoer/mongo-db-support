package com.koitoer.spring.mongodb.repository;

import com.koitoer.spring.mongodb.domain.B;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by mauricio.mena on 20/04/2016.
 */
public interface DocsBRepository extends MongoRepository<B,Long>{
}
