package za.ac.cput.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.user.Customer;

public interface ICustomerRepository extends JpaRepository <Customer, String> {
}
