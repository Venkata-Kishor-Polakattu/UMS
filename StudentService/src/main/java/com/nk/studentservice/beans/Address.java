package com.nk.studentservice.beans;

import lombok.Data;

@Data
public class Address {
    private String houseNumber;
    private String area;
    private String city;
    private Integer pinCode;
}
