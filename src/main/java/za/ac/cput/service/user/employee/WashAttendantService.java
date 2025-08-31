package za.ac.cput.service.user.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.repository.user.employee.IWashAttendantRepository;

import java.util.List;
import java.util.Random;

@Service
public class WashAttendantService implements IWashAttendantService {

    private IWashAttendantRepository washAttendantRepository;

    @Autowired
    public WashAttendantService(IWashAttendantRepository washAttendantRepository) {
        this.washAttendantRepository = washAttendantRepository;
    }

    @Override
    public WashAttendant create(WashAttendant washAttendant) {
        return washAttendantRepository.save(washAttendant);
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