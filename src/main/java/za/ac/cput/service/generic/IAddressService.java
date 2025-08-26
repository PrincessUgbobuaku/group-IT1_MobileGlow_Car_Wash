package za.ac.cput.service.generic;

/* MobileGlow Car Wash
   IAddress Service
   Author: Inga Zekani (221043756)
 */

import za.ac.cput.domain.generic.Address;
import za.ac.cput.service.IService;

import java.util.List;
import java.util.Optional;

public interface IAddressService extends IService<Address, Long> {

//    Address saveAddress(Address address);
    List<Address> getAllAddresses();
//    Optional<Address> getAddressById(Long id);
//    boolean existsById(Long id);
//    void deleteAddressById(Long id);

}
