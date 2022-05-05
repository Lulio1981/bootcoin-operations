package com.bootcamp.bootcoinoperations.service;

import com.bootcamp.bootcoinoperations.entity.Purse;
import com.bootcamp.bootcoinoperations.repository.PurseRepository;
import com.bootcamp.bootcoinoperations.util.Constant;
import com.bootcamp.bootcoinoperations.util.handler.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PurseServiceImpl implements PurseService {

    @Autowired
    public final PurseRepository repository;

    public Flux<Purse> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable("type-change")
    public Mono<Purse> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Purse> save(Purse purse) {
        return repository.findById(purse.getId())
                .map(sa -> {
                    throw new BadRequestException(
                            "ID",
                            "Type change exist must be upgrade",
                            sa.getId(),
                            PurseServiceImpl.class,
                            "save.onErrorResume"
                    );
                })
                .switchIfEmpty(Mono.defer(() -> {
                            purse.setId(null);
                            purse.setInsertionDate(new Date());
                            purse.setRegistrationStatus((short) 1);
                            return repository.save(purse);
                        }
                ))
                .onErrorResume(e -> Mono.error(e)).cast(Purse.class);
    }

    @Override
    public Mono<Purse> update(Purse purse) {
        return repository.findById(purse.getId())
                .switchIfEmpty(Mono.error(new Exception("An item with the id " + purse.getId() + " was not found. >> switchIfEmpty")))
                .flatMap(p -> repository.save(purse))
                .onErrorResume(e -> Mono.error(new BadRequestException(
                        "ID",
                        "An error occurred while trying to update an item.",
                        e.getMessage(),
                        PurseServiceImpl.class,
                        "update.onErrorResume"
                )));
    }

    @Override
    public Mono<Purse> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new Exception("An item with the id " + id + " was not found. >> switchIfEmpty")))
                .flatMap(p -> {
                    p.setRegistrationStatus(Constant.STATUS_INACTIVE);
                    return repository.save(p);
                })
                .onErrorResume(e -> Mono.error(new BadRequestException(
                        "ID",
                        "An error occurred while trying to delete an item.",
                        e.getMessage(),
                        PurseServiceImpl.class,
                        "update.onErrorResume"
                )));
    }

    @Override
    public Mono<Purse> getByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber);
    }

}
