package com.nk.studentservice.messaging.producer;

import com.nk.commoncontracts.events.StudentCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentEventProducer {

    private final KafkaTemplate<String, StudentCreatedEvent> kafkaTemplate;

    public void publishStudentCreatedEvent(StudentCreatedEvent event){
        kafkaTemplate.send("student-created",event);
    }
}
