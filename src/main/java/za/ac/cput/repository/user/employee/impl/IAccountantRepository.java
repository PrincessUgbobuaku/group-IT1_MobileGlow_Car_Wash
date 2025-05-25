package za.ac.cput.repository.user.employee.impl;



import za.ac.cput.domain.user.employee.Accountant;
import java.util.List;

public interface IAccountantRepository {
    Accountant create(Accountant accountant);
    Accountant read(String accountantID);
    Accountant update(Accountant accountant);
    boolean delete(String accountantID);
    List<Accountant> getAll();
}
