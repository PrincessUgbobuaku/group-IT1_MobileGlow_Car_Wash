package za.ac.cput.repository.generic;

/* MobileGlow Car Wash
   IContact Repository
   Author: Inga Zekani (221043756)
 */

import za.ac.cput.domain.generic.Contact;
import za.ac.cput.repository.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.generic.Contact;

import java.util.List;
import java.util.Optional;

@Repository
public interface IContactRepository extends JpaRepository<Contact, Long> {
    
}
