package za.ac.cput.service.generic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.repository.generic.IAddressRepository;
import za.ac.cput.service.generic.AddressService;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService implements IAddressService {

    private final IAddressRepository repository;

    @Autowired
    public AddressService(IAddressRepository repository) {

        this.repository = repository;
    }


    @Override
    public Address create(Address address) {
        return repository.save(address);
    }

    @Override
    public Address read(Long aLong) {
        return repository.findById(aLong).orElse(null);
    }

    @Override
    public Address update(Address address) {
        return repository.save(address);
    }

    @Override
    public boolean delete(Long aLong) {
        repository.deleteById(aLong);
        return true;
    }

    @Override
    public List<Address> getAllAddresses() {
        return repository.findAll();
    }


    }

