/*
    ManagerRepositoryImpl
Author: Abulele Voki(230778941)
Date: 11 May 2025
*/

package za.ac.cput.repository.user.employee;

import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.repository.user.employee.ManagerRepository;
import za.ac.cput.repository.user.employee.impl.IManagerRepository;

import java.util.*;

public class ManagerRepository implements IManagerRepository {
//
//    private static ManagerRepository repository = null;
//    private Map<String, Manager> managerMap;
//
//    private ManagerRepository() {
//        managerMap = new HashMap<>();
//    }
//
//    public static ManagerRepository getRepository() {
//        if (repository == null)
//            repository = new ManagerRepository();
//        return repository;
//    }
//
//    @Override
//    public Manager create(Manager manager) {
//        managerMap.put(manager.getManagerID(), manager);
//        return manager;
//    }
//
//    @Override
//    public Manager read(String managerID) {
//        return managerMap.get(managerID);
//    }
//
//    @Override
//    public Manager update(Manager manager) {
//        if (managerMap.containsKey(manager.getManagerID())) {
//            managerMap.put(manager.getManagerID(), manager);
//            return manager;
//        }
//        return null;
//    }
//
//    @Override
//    public boolean delete(String managerID) {
//        return managerMap.remove(managerID) != null;
//    }
//
//    @Override
//    public List<Manager> getAll() {
//        return new ArrayList<>(managerMap.values());
//    }
}
