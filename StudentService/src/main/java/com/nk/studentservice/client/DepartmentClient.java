package com.nk.studentservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "department-client",url = "")
public interface DepartmentClient {

}
