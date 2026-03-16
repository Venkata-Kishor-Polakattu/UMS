package com.nk.studentservice.messaging.consumer;

import com.nk.commoncontracts.events.DepartmentValidationFailedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DepartmentEventConsumer {

    @KafkaListener(topics = "dept-validation-failed",groupId = "student-group")
    public void handleDepartmentValidationFailedEvent(DepartmentValidationFailedEvent event){
        if (event.getMessage().startsWith("No")){
            System.out.println(event.getMessage());
        }else{
            System.out.println(event.getMessage());
            System.out.println(event.getAvailableDepartments());
        }
    }
}
