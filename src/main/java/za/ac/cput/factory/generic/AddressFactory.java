package za.ac.cput.factory.generic;
/* MobileGlow Car Wash
   Factory Address
   Author: Inga Zekani (221043756)
 */

import za.ac.cput.domain.generic.Address;
import java.util.UUID;
import za.ac.cput.util.Helper;

public class AddressFactory {

 public static Address createAddressFactory1( String streetNumber, String streetName, String city, String postalCode) {
     //Create Unique Address ID
     String addressID = UUID.randomUUID().toString();


     if (!Helper.isValidString(streetNumber) ||
             !Helper.isValidString(streetName) ||
             !Helper.isValidString(city) ||
             !Helper.isValidString(postalCode)) {
         return null;
     }

     return new Address.Builder()
             .setAddressID(addressID)
             .setStreetNumber(streetNumber)
             .setStreetName(streetName)
             .setCity(city)
             .setPostalCode(postalCode)
             .build();
    }

}
