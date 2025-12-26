package com.example.kafka_spring_boot.domain;


public record LibraryEvent (
        Integer libraryEventId,
        LibraryEventType libraryEventType,
        Book book

){



}
