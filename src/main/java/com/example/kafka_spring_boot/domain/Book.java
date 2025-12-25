package com.example.kafka_spring_boot.domain;

public record Book(
         Integer bookId,
         String bookName,
         String bookAuthor
) {
}
