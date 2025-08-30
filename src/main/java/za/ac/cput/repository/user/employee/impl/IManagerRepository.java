package za.ac.cput.repository.user.employee.impl;

import za.ac.cput.repository.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.user.employee.Manager;

@Repository
public interface IManagerRepository extends JpaRepository<Manager,Long> {

  //delete this class
}
