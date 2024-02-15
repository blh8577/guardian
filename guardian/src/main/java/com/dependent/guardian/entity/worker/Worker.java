package com.dependent.guardian.entity.worker;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workerIdx;
    private String userId;
    private String snsType;
    private String name;
    private String phone;
    private String camera;
    private String license;
    private String selfie;
    private String address1;
    private String address2;
    private String sex;
    private String introduce;
    private Float grade;



}
