package za.ac.cput.service.user.employee;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.repository.user.employee.IManagerRepository;
import za.ac.cput.service.user.UserService;

import java.io.IOException;
import java.util.List;

@Service
public class ManagerService implements IManagerService {

    private IManagerRepository managerRepository;

    @Autowired
    private UserService userService;


    @Autowired
    public ManagerService(IManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public Manager create(Manager manager) {
        Manager encryptManager = userService.encryptUserPassword(manager);
        return managerRepository.save(encryptManager);
    }

    @Override
    public Manager create(Manager manager, MultipartFile imageFile) throws IOException {
        Manager newManager = new Manager.Builder()
                .copy(manager)
                .setImageName(imageFile.getOriginalFilename())
                .setImageType(imageFile.getContentType())
                .setImage(imageFile.getBytes())
                .build();
        Manager encryptManager = userService.encryptUserPassword(newManager);
        return managerRepository.save(encryptManager);
    }

    @Override
    public Manager read(Long Id) {
        return managerRepository.findById(Id).orElse(null);
    }

    @Override
    public Manager update(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public Manager update(Manager manager, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            Manager newManager = new Manager.Builder()
                    .copy(manager)
                    .setImageName(imageFile.getOriginalFilename())
                    .setImageType(imageFile.getContentType())
                    .setImage(imageFile.getBytes())
                    .build();
            return managerRepository.save(newManager);
        } else {
            return managerRepository.save(manager);
        }
    }

    @Override
    public Manager updateManager(Long id, Manager updatedManager, MultipartFile imageFile) {
        Manager existing = managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found with ID: " + id));

        byte[] imageData = existing.getImageData();
        String imageName = existing.getImageName();
        String imageType = existing.getImageType();

        // Handle image update
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                imageData = imageFile.getBytes();
                imageName = imageFile.getOriginalFilename();
                imageType = imageFile.getContentType();
            } catch (IOException e) {
                throw new RuntimeException("Failed to process image file", e);
            }
        }

        // Handle login update - merge with existing login to preserve password and other fields
        Login updatedLogin = existing.getLogin();
        if (updatedManager.getLogin() != null && updatedManager.getLogin().getEmailAddress() != null) {
            updatedLogin = new Login.Builder()
                    .copy(existing.getLogin())
                    .setEmailAddress(updatedManager.getLogin().getEmailAddress())
                    .build();
        }

        // Build a new updated instance
        Manager updated = new Manager.Builder()
                .copy(existing)
                .setUserName(updatedManager.getUserName())
                .setUserSurname(updatedManager.getUserSurname())
                .setIsActive(updatedManager.getIsActive() != null ? updatedManager.getIsActive() : existing.getIsActive())
                .setRoleDescription(updatedManager.getRoleDescription() != null ? updatedManager.getRoleDescription() : existing.getRoleDescription())
                .setAddress(updatedManager.getAddress() != null ? updatedManager.getAddress() : existing.getAddress())
                .setContact(updatedManager.getContact() != null ? updatedManager.getContact() : existing.getContact())
                .setLogin(updatedLogin)
                .setEmployeeType(updatedManager.getEmployeeType() != null ? updatedManager.getEmployeeType() : existing.getEmployeeType())
                .setHireDate(updatedManager.getHireDate() != null ? updatedManager.getHireDate() : existing.getHireDate())
                .setImageName(imageName)
                .setImageType(imageType)
                .setImage(imageData)
                .build();

        return managerRepository.save(updated);
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public boolean delete(Long Id) {
        managerRepository.deleteById(Id);
        return true;
    }

   
}
