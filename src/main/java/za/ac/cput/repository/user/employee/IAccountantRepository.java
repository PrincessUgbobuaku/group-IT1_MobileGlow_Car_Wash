package za.ac.cput.repository.user.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.user.User;
import za.ac.cput.domain.user.employee.Accountant;
import za.ac.cput.repository.IRepository;

import java.util.List;

@Repository
public interface IAccountantRepository extends JpaRepository<Accountant, Long> {



}
