package za.ac.cput.repository.user.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.user.employee.WashAttendant;

public interface IWashAttendantRepository extends JpaRepository<WashAttendant,Long> {
}
