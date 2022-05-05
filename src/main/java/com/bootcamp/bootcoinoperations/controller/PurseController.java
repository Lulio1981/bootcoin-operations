package com.bootcamp.bootcoinoperations.controller;

import com.bootcamp.bootcoinoperations.entity.Purse;
import com.bootcamp.bootcoinoperations.service.PurseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("purse")
@Tag(name = "Type chamge", description = "Manage mainteance of purse")
@CrossOrigin(value = {"*"})
@RequiredArgsConstructor
public class PurseController {

    public final PurseService service;

    private RedisTemplate redisTemplate;

    @GetMapping
    public Mono<ResponseEntity<Flux<Purse>>> getAll() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.getAll())
        );
    }

    @GetMapping("/{idPurse}")
    public Mono<ResponseEntity<Mono<Purse>>> getByIdPurse(@PathVariable String idPurse) {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.getById(idPurse))
        );
    }

    @PostMapping
    public Mono<ResponseEntity<Purse>> create(@RequestBody Purse purse) {

        return service.save(purse).map(p -> ResponseEntity
                .created(URI.create("/Purse/".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(p)
        );
    }

    @PutMapping
    public Mono<ResponseEntity<Purse>> update(@RequestBody Purse purse) {
        return service.update(purse)
                .map(p -> ResponseEntity.created(URI.create("/Purse/"
                                .concat(p.getId())
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<ResponseEntity<Purse>> delete(@RequestBody String id) {
        return service.delete(id)
                .map(p -> ResponseEntity.created(URI.create("/Purse/"
                                .concat(p.getId())
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("documentNumber/{documentNumber}")
    public Mono<ResponseEntity<Mono<Purse>>> getByDocumentNumber(@PathVariable String documentNumber) {
        String key = "type_change" + documentNumber;
        ValueOperations<String, Purse> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);
        Purse purse = operations.get(key);


        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.getByDocumentNumber(documentNumber))
        );
    }
}
