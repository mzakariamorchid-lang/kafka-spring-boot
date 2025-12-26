package com.example.kafka_spring_boot.producer;

import com.example.kafka_spring_boot.domain.LibraryEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class LibraryEventProducer {


    @Value("${spring.kafka.topic}")
    private String topicName;

    private KafkaTemplate<Integer,String> kafkaTemplate;

    private ObjectMapper objectMapper;

    public LibraryEventProducer(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }
    //async
    public CompletableFuture<SendResult<Integer, String>> sendLibrarymessage(LibraryEvent libraryEvent) throws JsonProcessingException {
        var key = libraryEvent.libraryEventId();;
        var val = objectMapper.writeValueAsString(libraryEvent);


        //1 blocking call - get metadata about the kafka cluster
        //2 send message happens -return a completable future
        var completableFuture = kafkaTemplate.send(topicName,key,val);
        return completableFuture.whenComplete((SendResult, throwable) -> {
            if(throwable!=null){
                handleFailure(key,val,throwable);
            }else{
                handleSuccess(key,val,SendResult);
            }
        });
    }

    //sync
    public SendResult<Integer, String> sendLibrarymessage_approach2(LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {
        var key = libraryEvent.libraryEventId();;
        var val = objectMapper.writeValueAsString(libraryEvent);

        //1 blocking call - get metadata about the kafka cluster
        //2 Block and wait until the message is sent to the kafka
        var SendResult = kafkaTemplate.send(topicName,key,val).get();
        handleSuccess(key,val,SendResult);
        return SendResult;
    }

    //async
    public CompletableFuture<SendResult<Integer, String>> sendLibrarymessage_approuch3(LibraryEvent libraryEvent) throws JsonProcessingException {
        var key = libraryEvent.libraryEventId();;
        var val = objectMapper.writeValueAsString(libraryEvent);

        var producerRecord = buildProducerRecord(key,val);

        //1 blocking call - get metadata about the kafka cluster
        //2 send message happens -return a completable future
        var completableFuture = kafkaTemplate.send(producerRecord);

        return completableFuture.whenComplete((SendResult, throwable) -> {
            if(throwable!=null){
                handleFailure(key,val,throwable);
            }else{
                handleSuccess(key,val,SendResult);
            }
        });
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String val) {
        return new ProducerRecord<>(topicName,key,val);
    }

    private void handleSuccess(Integer key, String val, SendResult<Integer, String> sendResult) {
        log.info("Message Sent Successfully for the key {} and the value {} n parition is {}",
                key,val,sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(Integer key, String val, Throwable ex) {
        log.error("Error sending message {} ",ex.getMessage(),ex);
    }


}
