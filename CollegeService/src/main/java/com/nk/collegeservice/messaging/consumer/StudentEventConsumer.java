package com.nk.collegeservice.messaging.consumer;

import com.nk.collegeservice.service.CollegeService;
import com.nk.commoncontracts.events.StudentCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentEventConsumer {

    private final CollegeService collegeService;

    @KafkaListener(topics = "student-created",groupId = "college-group")
    public void handleStudentCreated(StudentCreatedEvent event){
        collegeService.updateStudentsCount(event);
    }
}
