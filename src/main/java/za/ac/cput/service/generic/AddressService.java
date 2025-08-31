package za.ac.cput.service.generic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.repository.generic.IAddressRepository;

import java.util.List;

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
    public Address read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Address update(Address address) {
        if (read(address.getAddressID()) == null) {
            return null; // or throw exception
        }
        return repository.save(address);
    }

    @Override
    public boolean delete(Long id) {
        if (read(id) != null) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Address> getAllAddresses() {
        return repository.findAll();
    }
}