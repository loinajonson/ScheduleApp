package com.example.myprojectscheduleapp;

public class AppointmentHelperClass {


    String appointmentemail, appointmentTime, appointmentDate;

    public String getAppointmentemail() {
        return appointmentemail;
    }
    public void setAppointmentemail(){
        this.appointmentemail = appointmentemail;
    }

    public String getAppointmentTime() {
    return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public AppointmentHelperClass( String appointmentemail, String appointmentTime, String appointmentDate){
        this.appointmentemail = appointmentemail;
        this.appointmentTime = appointmentTime;
        this.appointmentDate = appointmentDate;

    }

    public AppointmentHelperClass(){

    }
}
