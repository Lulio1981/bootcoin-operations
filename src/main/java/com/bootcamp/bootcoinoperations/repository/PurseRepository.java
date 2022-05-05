package com.bootcamp.bootcoinoperations.repository;

import com.bootcamp.bootcoinoperations.entity.Purse;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PurseRepository extends ReactiveCrudRepository<Purse, String> {

    Mono<Purse> findByDocumentNumber(String documentNumber);
}
