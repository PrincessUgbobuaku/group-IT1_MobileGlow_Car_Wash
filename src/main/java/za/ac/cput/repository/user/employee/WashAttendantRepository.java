/*
    WashAttendantRepositoryImpl
Author: Abulele Voki(230778941)
Date: 11 May 2025
*/

package za.ac.cput.repository.user.employee;

import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.repository.user.employee.impl.IWashAttendantRepository;

import java.util.*;

public class WashAttendantRepository implements IWashAttendantRepository {

    private static WashAttendantRepository repository = null;
    private Map<String, WashAttendant> washAttendantMap;

    private WashAttendantRepository() {
        washAttendantMap = new HashMap<>();
    }

    public static WashAttendantRepository getRepository() {
        if (repository == null)
            repository = new WashAttendantRepository();
        return repository;
    }

    @Override
    public WashAttendant create(WashAttendant washAttendant) {
        washAttendantMap.put(washAttendant.getWashAttendantID(), washAttendant);
        return washAttendant;
    }

    @Override
    public WashAttendant read(String attendantID) {
        return washAttendantMap.get(attendantID);
    }

    @Override
    public WashAttendant update(WashAttendant washAttendant) {
        if (washAttendantMap.containsKey(washAttendant.getWashAttendantID())) {
            washAttendantMap.put(washAttendant.getWashAttendantID(), washAttendant);
            return washAttendant;
        }
        return null;
    }

    @Override
    public boolean delete(String attendantID) {
        return washAttendantMap.remove(attendantID) != null;
    }

    @Override
    public List<WashAttendant> getAll() {
        return new ArrayList<>(washAttendantMap.values());
    }
}
