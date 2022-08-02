package com.example.anna.Models;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;

public class Subscription {

    private Tariff tariff;
    private Date dateStart, dateEnd;
    private User collaborator;
    private String status;



    public Subscription(Tariff tariff, Date dateStart, Date dateEnd, User collaborator) {
        this.tariff = tariff;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.collaborator = collaborator;
        this.status = getStatus();
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

    enum Status {
        ACTIVE,
        ENDED
    }

    public String getStatus() {
        Date currentDate = new java.util.Date();

        if (currentDate.compareTo(dateStart) > 0 && dateEnd.compareTo(currentDate) > 0) {
            return Status.ACTIVE.toString();
        }else
            return Status.ENDED.toString();
    }
}
