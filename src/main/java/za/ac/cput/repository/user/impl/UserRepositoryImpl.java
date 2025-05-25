package za.ac.cput.repository.user.impl;

import za.ac.cput.domain.user.User;
import za.ac.cput.repository.user.UserRepository;

import java.util.HashSet;
import java.util.Set;

public class UserRepositoryImpl implements UserRepository {

    private Set<User> usersDB;
    private static UserRepository userRepository;
    UserRepositoryImpl() {usersDB = new HashSet<User>();}

    public static UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = new UserRepositoryImpl();
        }
        return userRepository;
    }


    @Override
    public User create(User user) {
        this.usersDB.add(user);
        return user;
    }

    @Override
    public User read(String userId) {
        for (User user : usersDB) {
            user.getUserId().equals(userId);
            return user;
        }
        return null;
    }

    @Override
    public User update(User user) {
        User oldUser = this.read(user.getUserId());
        if (oldUser != null) {
            this.usersDB.remove(oldUser);
            this.usersDB.add(user);
        }
        return user;
    }

    @Override
    public void delete(String userId) {
        User user = this.read(userId);
        if (user != null) {
            this.usersDB.remove(user);
        }
    }

    @Override
    public Set<User> getUsers() {
        return usersDB;
    }
}
