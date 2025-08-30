package za.ac.cput.repository.user.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.user.employee.WashAttendant;


@Repository
public interface IWashAttendantRepository extends JpaRepository<WashAttendant,Long> {
}
