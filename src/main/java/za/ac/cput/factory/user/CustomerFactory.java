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

    public static Customer build1(LocalDate customerDOB) {
        if (!Helper.isValidDate(customerDOB)) {
            throw new IllegalArgumentException("CustomerDOB must not be null or a future date");
        }

        String generatedID = Helper.generateID();

        return new Customer.Builder()
                .setCustomerID(generatedID)
                .setCustomerDOB(customerDOB)
                .build();
    }
}
