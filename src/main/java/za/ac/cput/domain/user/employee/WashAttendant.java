/*
   WashAttendantDomain
Author: Abulele Voki(230778941)
Date: 11 May 2025
*/


package za.ac.cput.domain.user.employee;

public class WashAttendant {
    private String washAttendantID;
    private boolean isFullTime;
    private int shiftHours;


    public WashAttendant(){


    }

    public WashAttendant(String washAttendantID, boolean isFullTime, int shiftHours) {
        this.washAttendantID = washAttendantID;
        this.isFullTime = isFullTime;
        this.shiftHours = shiftHours;
    }

    public String getWashAttendantID() {
        return washAttendantID;
    }

    public void setWashAttendantID(String washAttendantID) {
        this.washAttendantID = washAttendantID;
    }

    public boolean isFullTime() {
        return isFullTime;
    }

    public void setFullTime(boolean fullTime) {
        isFullTime = fullTime;
    }

    public int getShiftHours() {
        return shiftHours;
    }

    public void setShiftHours(int shiftHours) {
        this.shiftHours = shiftHours;
    }

}
