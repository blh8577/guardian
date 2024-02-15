package com.dependent.guardian.entity.address;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Address {
    //정렬은 따로 생각해볼것..
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressIdx;
    private String address1;
    private String address2;


}
