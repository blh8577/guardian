package com.dependent.guardian.entity.request;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestIdx;
    private String review;
    private String name;
    private String sex;
    private String disease;
    private String age;
    private String hospital;
    private String detail;
    private String condition;
    private LocalDateTime date;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Float grade;
    private Integer memberIdx;
    private Integer workerIdx;


}
