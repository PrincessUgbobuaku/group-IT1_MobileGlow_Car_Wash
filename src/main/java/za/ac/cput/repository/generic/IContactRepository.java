package za.ac.cput.repository.generic;

/* MobileGlow Car Wash
   IContact Repository
   Author: Inga Zekani (221043756)
 */

import za.ac.cput.domain.generic.Contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IContactRepository extends JpaRepository<Contact, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
}
