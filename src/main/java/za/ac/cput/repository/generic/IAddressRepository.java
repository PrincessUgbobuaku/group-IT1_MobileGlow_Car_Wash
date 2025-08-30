package za.ac.cput.repository.generic;

/* MobileGlow Car Wash
   IAddress Repository
   Author: Inga Zekani (221043756)
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.generic.Address;

@Repository
public interface IAddressRepository extends JpaRepository<Address, Long> {
    // You can add custom query methods here if needed


}
