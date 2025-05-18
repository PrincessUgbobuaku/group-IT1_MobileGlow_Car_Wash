/*
Student name: Thaakirah Watson
Student number: 230037550
Description: Factory class for Customer
 */

package za.ac.cput.factory.user;

import za.ac.cput.domain.user.Customer;
import za.ac.cput.util.Helper;
import java.time.LocalDate;

public class CustomerFactory {

    public static Customer build(String customerID, LocalDate customerDOB) {
        Helper.validateCustomerFields(customerID, customerDOB);

        return new Customer.Builder()
                .setCustomerID(customerID)
                .setCustomerDOB(customerDOB)
                .build();
    }
}
