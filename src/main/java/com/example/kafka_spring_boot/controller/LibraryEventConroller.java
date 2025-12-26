package com.example.kafka_spring_boot.controller;

import com.example.kafka_spring_boot.domain.LibraryEvent;
import com.example.kafka_spring_boot.producer.LibraryEventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@Slf4j
public class LibraryEventConroller {


    @Autowired
    LibraryEventProducer libraryEventProducer;

    @PostMapping("/libraryevent")
    public ResponseEntity<LibraryEvent> saveLibrary(
            @RequestBody
            LibraryEvent libraryEvent){
        log.info("LibraryEvent : {}",libraryEvent);

        try {
            libraryEventProducer.sendLibrarymessage(libraryEvent);
        } catch (JsonProcessingException e) {
            log.error("Erreur d'envoie");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
