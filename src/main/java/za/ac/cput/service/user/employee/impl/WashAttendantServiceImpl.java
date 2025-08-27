package za.ac.cput.service.user.employee.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.repository.user.employee.IWashAttendantRepository;
import za.ac.cput.service.user.employee.IWashAttendantService;

import java.util.List;

@Service
public class WashAttendantServiceImpl implements IWashAttendantService {

    private IWashAttendantRepository washAttendantRepository;

    @Autowired
    public WashAttendantServiceImpl(IWashAttendantRepository washAttendantRepository) {
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
}