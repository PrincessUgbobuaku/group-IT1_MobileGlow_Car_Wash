package za.ac.cput.domain.user.employee;

import com.sun.source.tree.UsesTree;

import java.util.Date;

public class Manager {
    private String managerID;
    private Date hireDate;


    private Manager(Builder builder){
        this.managerID=builder.managerID;
        this.hireDate = builder.hireDate;

    }
//    getters
    public String getManagerID() {
        return managerID;
    }
    public Date getHireDate(){
        return hireDate;
    }
//2string
    @Override
    public String toString() {
        return "Manager{" +
                "managerID='" + managerID + '\'' +
                ", hireDate=" + hireDate +
                '}';
    }

    public static class Builder{
        private String managerID;
        private Date hireDate;


        public Builder setManagerID(String managerID){
            this.managerID=managerID;
            return this;

        }

        public Builder setHireDate(Date hireDate){
            this.hireDate=hireDate;
            return this;

        }
        public Manager build(){
            return new Manager(this);
        }
    }
}
