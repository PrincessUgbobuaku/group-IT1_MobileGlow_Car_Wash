/*
    WashAttendantDomain
Author: Abulele Voki(230778941)
Date: 11 May 2025
*/

package za.ac.cput.domain.user.employee;

import jakarta.persistence.Entity;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;

import java.time.LocalDate;
import java.util.Arrays;

@Entity
public class WashAttendant extends User {
    private boolean isFullTime;
    private int shiftHours;
    private String employeeType;
    private LocalDate hireDate;

    protected WashAttendant() {
        super();
    }

    private WashAttendant(Builder builder){
        this.userId = builder.userId;
        this.userName = builder.userName;
        this.userSurname = builder.userSurname;
        this.isActive = builder.isActive;
        this.roleDescription = builder.roleDescription;
        this.employeeType = builder.employeeType;
        this.isFullTime = builder.isFullTime;
        this.shiftHours = builder.shiftHours;
        this.hireDate = builder.hireDate;
        this.imageName = builder.imageName;
        this.imageType = builder.imageType;
        this.imageData = builder.imageData;
        this.contact = builder.contact;
        this.address = builder.address;
        this.login = builder.login;
    }

    public boolean getIsFullTime() {
        return isFullTime;
    }

    public int getShiftHours() {
        return shiftHours;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    @Override
    public String toString() {
        return "WashAttendant{" +
                "isFullTime=" + isFullTime +
                ", shiftHours=" + shiftHours +
                ", employeeType='" + employeeType + '\'' +
                ", hireDate=" + hireDate +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userSurname='" + userSurname + '\'' +
                ", isActive=" + isActive +
                ", imageName='" + imageName + '\'' +
                ", imageType='" + imageType + '\'' +
                ", imageData=" + Arrays.toString(imageData) +
                ", roleDescription=" + roleDescription +
                ", contact=" + contact +
                ", address=" + address +
                ", login=" + login +
                '}';
    }

    public static class Builder {
        private Long userId;
        private String userName;
        private String userSurname;
        private Boolean isActive;
        private User.RoleDescription roleDescription;
        private String employeeType;
        private boolean isFullTime;
        private int shiftHours;
        private LocalDate hireDate;
        private String imageName;
        private String imageType;
        private byte[] imageData;
        private Contact contact;
        private Address address;
        private Login login;

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setUserSurname(String userSurname) {
            this.userSurname = userSurname;
            return this;
        }

        public Builder setIsActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder setRoleDescription(User.RoleDescription roleDescription) {
            this.roleDescription = roleDescription;
            return this;
        }

        public Builder setEmployeeType(String employeeType) {
            this.employeeType = employeeType;
            return this;
        }

        public Builder setIsFullTime(boolean isFullTime) {
            this.isFullTime = isFullTime;
            return this;
        }

        public Builder setShiftHours(int shiftHours) {
            this.shiftHours = shiftHours;
            return this;
        }

        public Builder setHireDate(LocalDate hireDate) {
            this.hireDate = hireDate;
            return this;
        }

        public Builder setImageName(String imageName) {
            this.imageName = imageName;
            return this;
        }

        public Builder setImageType(String imageType) {
            this.imageType = imageType;
            return this;
        }

        public Builder setImageData(byte[] imageData) {
            this.imageData = imageData;
            return this;
        }

        public Builder setContact(Contact contact) {
            this.contact = contact;
            return this;
        }

        public Builder setAddress(Address address) {
            this.address = address;
            return this;
        }

        public Builder setLogin(Login login) {
            this.login = login;
            return this;
        }

        public Builder copy(WashAttendant washAttendant) {
            this.userId = washAttendant.userId;
            this.userName = washAttendant.userName;
            this.userSurname = washAttendant.userSurname;
            this.isActive = washAttendant.isActive;
            this.roleDescription = washAttendant.roleDescription;
            this.employeeType = washAttendant.employeeType;
            this.isFullTime = washAttendant.isFullTime;
            this.shiftHours = washAttendant.shiftHours;
            this.hireDate = washAttendant.hireDate;
            this.imageName = washAttendant.imageName;
            this.imageType = washAttendant.imageType;
            this.imageData = washAttendant.imageData;
            this.contact = washAttendant.contact;
            this.address = washAttendant.address;
            this.login = washAttendant.login;
            return this;
        }

        public WashAttendant build() {
            return new WashAttendant(this);
        }
    }
}