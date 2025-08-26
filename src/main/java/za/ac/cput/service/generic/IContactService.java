package za.ac.cput.service.generic;

/* MobileGlow Car Wash
   IContact Service
   Author: Inga Zekani (221043756)
 */

import za.ac.cput.domain.generic.Contact;
import za.ac.cput.service.IService;

import java.util.List;

public interface IContactService extends IService<Contact, Long> {

    List<Contact> getAll();

}