package com.nk.departmentservice.messaging.producer;

import com.nk.commoncontracts.events.DepartmentValidationFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentEventProducer {

    private final KafkaTemplate<String, DepartmentValidationFailedEvent> kafkaTemplate;

    public void sendDepartmentValidationFailedEvent(DepartmentValidationFailedEvent event) {

        kafkaTemplate.send("dept-validation-failed", event);

    }
}
