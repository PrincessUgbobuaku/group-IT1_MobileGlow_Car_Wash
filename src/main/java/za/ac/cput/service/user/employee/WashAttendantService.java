package za.ac.cput.service.user.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.repository.user.employee.IWashAttendantRepository;
import za.ac.cput.service.user.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
public class WashAttendantService implements IWashAttendantService {

    private IWashAttendantRepository washAttendantRepository;

    @Autowired
    private UserService userService;

    @Autowired
    public WashAttendantService(IWashAttendantRepository washAttendantRepository) {
        this.washAttendantRepository = washAttendantRepository;
    }

    @Override
    public WashAttendant create(WashAttendant washAttendant) {
        WashAttendant encryptedWashAttendant = userService.encryptUserPassword(washAttendant);
        return washAttendantRepository.save(encryptedWashAttendant);
    }

    @Override
    public WashAttendant read(Long Id) {
        return washAttendantRepository.findById(Id).orElse(null);
    }

    @Override
    public WashAttendant update(WashAttendant washAttendant) {
        return washAttendantRepository.save(washAttendant);
    }

    @Override
    public WashAttendant update(WashAttendant washAttendant, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            WashAttendant newWashAttendant = new WashAttendant.Builder()
                    .copy(washAttendant)
                    .setImageName(imageFile.getOriginalFilename())
                    .setImageType(imageFile.getContentType())
                    .setImageData(imageFile.getBytes())
                    .build();
            return washAttendantRepository.save(newWashAttendant);
        } else {
            return washAttendantRepository.save(washAttendant);
        }
    }

    @Override
    public WashAttendant updateWashAttendant(Long id, WashAttendant updatedWashAttendant, MultipartFile imageFile) {
        WashAttendant existing = washAttendantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WashAttendant not found with ID: " + id));

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
        if (updatedWashAttendant.getLogin() != null && updatedWashAttendant.getLogin().getEmailAddress() != null) {
            updatedLogin = new Login.Builder()
                    .copy(existing.getLogin())
                    .setEmailAddress(updatedWashAttendant.getLogin().getEmailAddress())
                    .build();
        }

        // Build a new updated instance
        WashAttendant updated = new WashAttendant.Builder()
                .copy(existing)
                .setUserName(updatedWashAttendant.getUserName())
                .setUserSurname(updatedWashAttendant.getUserSurname())
                .setIsActive(updatedWashAttendant.getIsActive() != null ? updatedWashAttendant.getIsActive() : existing.getIsActive())
                .setRoleDescription(updatedWashAttendant.getRoleDescription() != null ? updatedWashAttendant.getRoleDescription() : existing.getRoleDescription())
                .setAddress(updatedWashAttendant.getAddress() != null ? updatedWashAttendant.getAddress() : existing.getAddress())
                .setContact(updatedWashAttendant.getContact() != null ? updatedWashAttendant.getContact() : existing.getContact())
                .setLogin(updatedLogin)
                .setEmployeeType(updatedWashAttendant.getEmployeeType() != null ? updatedWashAttendant.getEmployeeType() : existing.getEmployeeType())
                .setIsFullTime(updatedWashAttendant.getIsFullTime())
                .setShiftHours(updatedWashAttendant.getShiftHours())
                .setImageName(imageName)
                .setImageType(imageType)
                .setImageData(imageData)
                .build();

        return washAttendantRepository.save(updated);
    }

    @Override
    public List<WashAttendant> getAllWashAttendants() {
        return washAttendantRepository.findAll();
    }

    @Override
    public boolean delete(Long Id) {
        washAttendantRepository.deleteById(Id);
        return false;
    }


    @Override
    public WashAttendant getRandomWashAttendant() {
        List<WashAttendant> allAttendants = washAttendantRepository.findAll();

        if (allAttendants.isEmpty()) {
            return null; // Or throw exception if you prefer
        }

        Random random = new Random();
        int randomIndex = random.nextInt(allAttendants.size());
        return allAttendants.get(randomIndex);
    }

//    @Override
//    public List<WashAttendant> findAll() {
//        return List.of();
//    }
}