package com.nk.studentservice.client;

import com.nk.commoncontracts.respnse.CollegeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "college-service", url = "${services.college.url}")
public interface CollegeClient {

    @GetMapping("/college/check/{cid}")
    CollegeResponseDto checkCollege(@PathVariable String cid);
}

