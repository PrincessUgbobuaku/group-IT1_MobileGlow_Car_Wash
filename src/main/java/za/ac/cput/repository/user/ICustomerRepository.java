//Thaakirah Watson, 230037550
package za.ac.cput.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.user.Customer;

import java.util.List;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Long> {
    // Custom queries
    List<Customer> findByUserSurname(String userSurname);

    List<Customer> findByIsActive(Boolean isActive);
}
