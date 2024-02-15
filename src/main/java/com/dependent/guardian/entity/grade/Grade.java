package com.dependent.guardian.entity.grade;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gradeIdx;
    private Integer point;
    private LocalDateTime date;
    private Integer requestIdx;
    private Integer workerIdx;

}
