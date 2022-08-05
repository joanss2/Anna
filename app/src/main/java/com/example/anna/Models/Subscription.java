package com.example.anna.Models;

import android.annotation.SuppressLint;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Subscription{

    private Tariff tariff;
    private Date dateStart, dateEnd;
    private User collaborator;
    private String status;



    public Subscription(Tariff tariff, Date dateStart, Date dateEnd, User collaborator, String status){

        String format = "MM/dd/yyyy" ;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format) ;

        this.tariff = tariff;

        this.dateStart = dateStart;
        this.dateEnd = dateEnd;


        this.collaborator = collaborator;
        this.status = status;
    }

    public Subscription() {
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public Date getDateStart() {
        return dateStart;
    }
    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }
    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }


    public User getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(User collaborator) {
        this.collaborator = collaborator;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Subscription (Map<String,Object> map){
        Subscription sub = new Subscription();

        Map<String,Object> collaborator = (Map<String, Object>) map.get("collaborator");
        String userKey = (String)collaborator.get("userKey");
        String username = (String)collaborator.get("username");
        String email = (String)collaborator.get("email");
        String language = (String)collaborator.get("language");


        User user = new User(username,email,userKey,language);

        Date date1 = ((Timestamp)map.get("dateStart")).toDate();
        Date date2 = ((Timestamp)map.get("dateEnd")).toDate();
        String status = (String)map.get("status");

        this.collaborator = user;
        this.dateStart = date1;
        this.dateEnd = date2;
        this.status = status;
    }

}
