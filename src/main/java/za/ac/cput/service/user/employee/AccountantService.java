package za.ac.cput.service.user.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.employee.Accountant;
//import za.ac.cput.repository.user.employee.impl.IAccountantRepository;
import za.ac.cput.repository.user.employee.IAccountantRepository;
import za.ac.cput.service.user.UserService;
import za.ac.cput.service.user.employee.IAccountantService;

import java.io.IOException;
import java.util.List;

@Service
public class AccountantService implements IAccountantService {

    private final IAccountantRepository accountantRepository;

    @Autowired
    private UserService userService;

    @Autowired
    public AccountantService(IAccountantRepository accountantRepository) {
        this.accountantRepository = accountantRepository;
    }

    @Override
    public Accountant create(Accountant accountant) {
        Accountant encryptAccountant = userService.encryptUserPassword(accountant);
        return accountantRepository.save(encryptAccountant);
    }

    @Override
    public Accountant read(Long Id) {
        return accountantRepository.findById(Id).orElse(null);
    }

    @Override
    public Accountant update(Accountant accountant) {
        return accountantRepository.save(accountant);
    }

    @Override
    public Accountant update(Accountant accountant, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            Accountant newAccountant = new Accountant.Builder()
                    .copy(accountant)
                    .setImageName(imageFile.getOriginalFilename())
                    .setImageType(imageFile.getContentType())
                    .setImageData(imageFile.getBytes())
                    .build();
            return accountantRepository.save(newAccountant);
        } else {
            return accountantRepository.save(accountant);
        }
    }

    @Override
    public List<Accountant> getAllAccountants() {
        return accountantRepository.findAll();
    }

    @Override
    public boolean delete(Long Id) {
        accountantRepository.deleteById(Id);
        return false;
    }

    @Override
    public Accountant updateAccountant(Long id, Accountant updatedAccountant, MultipartFile imageFile) {
        Accountant existing = accountantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accountant not found with ID: " + id));

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
        if (updatedAccountant.getLogin() != null && updatedAccountant.getLogin().getEmailAddress() != null) {
            updatedLogin = new Login.Builder()
                    .copy(existing.getLogin())
                    .setEmailAddress(updatedAccountant.getLogin().getEmailAddress())
                    .build();
        }

        // Build a new updated instance
        Accountant updated = new Accountant.Builder()
                .copy(existing)
                .setUserName(updatedAccountant.getUserName())
                .setUserSurname(updatedAccountant.getUserSurname())
                .setIsActive(updatedAccountant.getIsActive() != null ? updatedAccountant.getIsActive() : existing.getIsActive())
                .setRoleDescription(updatedAccountant.getRoleDescription() != null ? updatedAccountant.getRoleDescription() : existing.getRoleDescription())
                .setAddress(updatedAccountant.getAddress() != null ? updatedAccountant.getAddress() : existing.getAddress())
                .setContact(updatedAccountant.getContact() != null ? updatedAccountant.getContact() : existing.getContact())
                .setLogin(updatedLogin)
                .setEmployeeType(updatedAccountant.getEmployeeType() != null ? updatedAccountant.getEmployeeType() : existing.getEmployeeType())
                .setHasTaxFillingAuthority(updatedAccountant.getHasTaxFillingAuthority())
                .setImageName(imageName)
                .setImageType(imageType)
                .setImageData(imageData)
                .build();

        return accountantRepository.save(updated);
    }

   
}

