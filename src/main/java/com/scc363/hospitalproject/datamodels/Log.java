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
    private String date;

    @Column
    private String time;

    @Column
    private String level;

    @Column
    private String message;

    @Column
    private String userName;

    public Log() {

    }


    public Log(LocalDate date, LocalTime time, String level, String message, String userName){
        this.date = date.toString();
        this.time = time.toString();
        this.level = level;
        this.message = message;
        this.userName = userName;
    }

//
//    @Override
//
//    public String toString() {
//        return date + " " + time + " " + level + " " + message + " " + userName;
//    }

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }

    public String getLevel(){
        return level;
    }

    public String getMessage(){
        return message;
    }

    public String getUserName(){
        return userName;
    }
}
