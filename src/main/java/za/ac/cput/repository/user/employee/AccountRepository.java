package za.ac.cput.repository.user.employee;

import za.ac.cput.domain.user.employee.Accountant;
import za.ac.cput.repository.user.employee.AccountantRepository;
import za.ac.cput.repository.user.employee.impl.IAccountantRepository;

import java.util.*;

public class AccountantRepository implements IAccountantRepository {

//    private static AccountantRepository repository = null;
//    private Map<String, Accountant> accountantMap;
//
//    private AccountantRepository() {
//        accountantMap = new HashMap<>();
//    }
//
//    public static AccountantRepository getRepository() {
//        if (repository == null)
//            repository = new AccountantRepository();
//        return repository;
//    }
//
//    @Override
//    public Accountant create(Accountant accountant) {
//        accountantMap.put(accountant.getAccountantID(), accountant);
//        return accountant;
//    }
//
//    @Override
//    public Accountant read(String accountantID) {
//        return accountantMap.get(accountantID);
//    }
//
//    @Override
//    public Accountant update(Accountant accountant) {
//        if (accountantMap.containsKey(accountant.getAccountantID())) {
//            accountantMap.put(accountant.getAccountantID(), accountant);
//            return accountant;
//        }
//        return null;
//    }
//
//    @Override
//    public boolean delete(String accountantID) {
//        Accountant removed = accountantMap.remove(accountantID);
//        return removed != null;
//    }
//
//    @Override
//    public List<Accountant> getAll() {
//        return new ArrayList<>(accountantMap.values());
//    }
}
