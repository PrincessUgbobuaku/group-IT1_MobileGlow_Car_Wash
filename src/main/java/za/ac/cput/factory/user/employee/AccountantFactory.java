package za.ac.cput.factory.user.employee;

import za.ac.cput.domain.user.employee.Accountant;
import za.ac.cput.util.Helper;



public class AccountantFactory {
    public static Accountant createAccountant(boolean hasTaxFillingAuthority) {
        String accountantID = Helper.generateID();
        if(!Helper.validateHasTaxFillingAuthority(hasTaxFillingAuthority)){
            return null;
        }


        return new Accountant.Builder()
                .setAccountantID(accountantID)
                .setHasTaxFillingAuthority(hasTaxFillingAuthority)
                .build();
    }
}
