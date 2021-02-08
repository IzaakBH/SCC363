package com.scc363.hospitalproject.datamodels;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer errorId;

    @Column
    private LocalDateTime date;

    @Column
    private String level;

    @Column
    private String message;

    @Column
    private String userName;

    public Log(LocalDateTime date, String level, String message, String userName){
        this.date = date;
        this.level = level;
        this.message = message;
        this.userName = userName;
    }

}
