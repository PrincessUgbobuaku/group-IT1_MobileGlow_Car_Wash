/*
    MavagerDomain
Author: Abulele Voki(230778941)
Date: 11 May 2025
*/

package za.ac.cput.domain.user.employee;

import java.util.Date;

public class Manager {
    private String managerID;
    private Date hireDate;


    public Manager(){

    }
    public Manager(String managerID,Date hireDate){
        this.managerID=managerID;
        this.hireDate=hireDate;
    }

    public String getManagerID() {
        return managerID;
    }

    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
}
