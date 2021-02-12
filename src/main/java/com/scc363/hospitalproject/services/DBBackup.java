package com.scc363.hospitalproject.services;

import com.scc363.hospitalproject.datamodels.Log;
import com.scc363.hospitalproject.repositories.LogsRepository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DBBackup {

    private boolean checkDB = false;
    LogsRepository logsRepository;

    public void backupDB() {

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int day = localDate.getDayOfMonth();

        if (day == 11 && checkDB == false) {
            try {
                Class.forName("org.h2.Driver");
                Connection con = DriverManager.getConnection("jdbc:h2:" + "./data/userdata", "sa", "password");
                Statement stmt = con.createStatement();
                con.prepareStatement("BACKUP TO 'backup.zip'").executeUpdate();
                checkDB = true;
                System.out.println("hola");
                logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "debug", "Database backup created", null));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.toString());
                logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "Problem backing up the db", null));
            }
        }
        if (day == 2){
            checkDB= false;
        }
    }

}
