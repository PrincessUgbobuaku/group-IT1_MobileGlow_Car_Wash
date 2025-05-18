package za.ac.cput.factory.user.employee;

import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.util.Helper;

import java.util.UUID;

public class WashAttendantFactory {
    public static WashAttendant createWashAttendant(boolean isFullTime, int shiftHours) {
        String washAttendantID = Helper.generateID();{
            if(!Helper.validateShiftHours(shiftHours)
                    ||Helper.validateIsFullTime(isFullTime)){
                return null;
            }

        }


        return new WashAttendant.Builder()
                .setWashAttendantID(washAttendantID)
                .setisFullTime(isFullTime)
                .setShiftHours(shiftHours)
                .build();



    }
}
