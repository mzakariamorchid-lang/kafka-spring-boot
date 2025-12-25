package com.example.kafka_spring_boot;

import com.example.kafka_spring_boot.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class LibraryEventConroller {


    @PostMapping("/save")
    public ResponseEntity<LibraryEvent> saveLibrary(
            @RequestBody
            LibraryEvent libraryEvent){
        log.info("LibraryEvent : {}",libraryEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
