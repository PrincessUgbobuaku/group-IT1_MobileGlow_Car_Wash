package za.ac.cput.service.user.employee;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.domain.user.User;
import za.ac.cput.domain.user.employee.Accountant;
import za.ac.cput.service.IService;

import java.io.IOException;
import java.util.List;

@Service
public interface IAccountantService extends IService<Accountant, Long> {


    List<Accountant> getAllAccountants();

    Accountant update(Accountant accountant, MultipartFile imageFile) throws IOException;

    Accountant updateAccountant(Long id, Accountant updatedAccountant, MultipartFile imageFile);




}
