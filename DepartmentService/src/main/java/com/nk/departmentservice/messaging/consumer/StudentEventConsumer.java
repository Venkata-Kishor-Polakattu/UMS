package com.nk.departmentservice.messaging.consumer;

import com.nk.commoncontracts.events.StudentCreatedEvent;
import com.nk.departmentservice.service.DepartmentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentEventConsumer {

    private final DepartmentServiceImpl departmentService;

    @KafkaListener(topics = "student-created",groupId = "department-group")
    public void handleStudentCreated(StudentCreatedEvent event){
        departmentService.increaseStudentCount(event);
    }
}
