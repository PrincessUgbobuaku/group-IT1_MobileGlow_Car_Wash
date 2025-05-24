package za.ac.cput.factory.user.employee;

import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.util.Helper;

import java.util.Date;
import java.util.UUID;

public class ManagerFactory {
    public static Manager createManager(Date hireDate) {
        String managerId = Helper.generateID();{
            if(!Helper.validateDate(hireDate)) {

                return null;
            }

        }


        return new Manager.Builder().
                setHireDate(hireDate).
                setManagerID(managerId).
                build();


    }

}
