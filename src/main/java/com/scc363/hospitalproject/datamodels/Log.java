package com.scc363.hospitalproject.datamodels;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer errorId;

    @Column
    private LocalDate date;

    @Column
    private LocalTime time;

    @Column
    private String level;

    @Column
    private String message;

    @Column
    private String userName;

    public Log() {

    }


    public Log(LocalDate date, LocalTime time, String level, String message, String userName){
        this.date = date;
        this.time = time;
        this.level = level;
        this.message = message;
        this.userName = userName;
    }


    @Override

    public String toString(){
        return String.format("[Log:   date:  "+ date + "  time:  " + time + "  level  "+ level+ "  message:"+message+ "  username "+userName +" ]");
    }

    public LocalDate getDate(){
        return date;
    }

    public LocalTime getTime(){
        return time;
    }

    public String getLevel(){
        return level;
    }

    public String getMessage(){
        return message;
    }

    public String getUsername(){
        return userName;
    }
}
