package za.ac.cput.service.user.employee;

import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.service.IService;

import java.io.IOException;
import java.util.List;

public interface IWashAttendantService extends IService<WashAttendant, Long> {
    List<WashAttendant> getAllWashAttendants();

    WashAttendant getRandomWashAttendant();

    WashAttendant update(WashAttendant washAttendant, MultipartFile imageFile) throws IOException;

    WashAttendant updateWashAttendant(Long id, WashAttendant updatedWashAttendant, MultipartFile imageFile);

//    List<WashAttendant> findAll();
}
