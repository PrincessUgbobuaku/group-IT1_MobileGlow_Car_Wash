package za.ac.cput.repository.generic;

import za.ac.cput.domain.generic.Address;
import za.ac.cput.repository.generic.impl.IAddressRepository;

import java.util.ArrayList;
import java.util.List;

public class AddressRepository implements IAddressRepository {

    private final List<Address> addressList = new ArrayList<>();

    @Override
    public Address create(Address address) {
        addressList.add(address);
        return address;
    }

    @Override
    public Address read(String addressID) {
        return addressList.stream()
                .filter(a -> a.getAddressID().equals(addressID))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Address update(Address address) {
        Address existing = read(address.getAddressID());
        if (existing != null) {
            addressList.remove(existing);
            addressList.add(address);
            return address;
        }
        return null;
    }

    @Override
    public boolean delete(String addressID) {
        Address address = read(addressID);
        return address != null && addressList.remove(address);
    }
}

