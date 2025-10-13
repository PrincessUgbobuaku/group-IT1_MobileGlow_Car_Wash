package za.ac.cput.domain.user.employee;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import jakarta.persistence.Entity;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;

import java.time.LocalDate;
import java.util.Arrays;

@Entity
public class Manager extends User {
    private LocalDate hireDate;
    private String employeeType;

    protected Manager() {
        super();
    }

    private Manager(Builder builder){
        this.userId=builder.userId;
        this.userName=builder.userName;
        this.userSurname=builder.userSurname;
        this.isActive=builder.isActive;
        this.roleDescription=builder.roleDescription;
        this.employeeType=builder.employeeType;
        this.hireDate = builder.hireDate;
        this.imageName=builder.imageName;
        this.imageType=builder.imageType;
        this.imageData=builder.imageData;
        this.contact=builder.contact;
        this.address=builder.address;
        this.login=builder.login;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public LocalDate getHireDate(){
        return hireDate;
    }


    @Override
    public String toString() {
        return "Manager{" +
                "hireDate=" + hireDate +
                ", employeeType='" + employeeType + '\'' +
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

    public static class Builder{
        private Long userId;
        private String userName;
        private String userSurname;
        private Boolean isActive;
        private User.RoleDescription roleDescription;
        private String employeeType;
        private LocalDate hireDate;
        private String imageName;
        private String imageType;
        private byte[] imageData;
        private Contact contact;
        private Address address;
        private Login login;

        public Builder setUserId(Long userId){
            this.userId = userId;
            return this;
        }
        public Builder setUserName(String userName){
            this.userName = userName;
            return this;
        }
        public Builder setUserSurname(String userSurname){
            this.userSurname = userSurname;
            return this;
        }

        public Builder setIsActive(Boolean isActive){
            this.isActive = isActive;
            return this;
        }
        public Builder setRoleDescription(User.RoleDescription roleDescription){
            this.roleDescription = roleDescription;
            return this;
        }

        public Builder setEmployeeType(String employeeType){
            this.employeeType = employeeType;
            return this;
        }

        public Builder setHireDate(LocalDate hireDate){
            this.hireDate=hireDate;
            return this;
        }

        public Builder setImageName(String imageName){
            this.imageName=imageName;
            return this;
        }

        public Builder setImageType(String imageType){
            this.imageType=imageType;
            return this;
        }

        public Builder setImage(byte[] imageData){
            this.imageData=imageData;
            return this;
        }

        public Builder setContact(Contact contact){
            this.contact=contact;
            return this;
        }

        public Builder setAddress(Address address){
            this.address=address;
            return this;
        }

        public Builder setLogin(Login login){
            this.login=login;
            return this;
        }

        public Builder copy(Manager manager){
            this.userId=manager.userId;
            this.userName=manager.userName;
            this.userSurname=manager.userSurname;
            this.isActive=manager.isActive;
            this.roleDescription=manager.roleDescription;
            this.employeeType=manager.employeeType;
            this.hireDate=manager.hireDate;
            this.imageName=manager.imageName;
            this.imageType=manager.imageType;
            this.imageData=manager.imageData;
            this.contact=manager.contact;
            this.address=manager.address;
            this.login=manager.login;
            return this;
        }

        public Manager build(){
            return new Manager(this);
        }
    }
}



