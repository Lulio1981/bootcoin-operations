package com.bootcamp.bootcoinoperations.service;

import com.bootcamp.bootcoinoperations.entity.Purse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PurseService {

    public Flux<Purse> getAll();

    public Mono<Purse> getById(String id);

    public Mono<Purse> save(Purse purse);

    public Mono<Purse> update(Purse purse);

    public Mono<Purse> delete(String id);

    Mono<Purse> getByDocumentNumber(String documentNumber);

}
