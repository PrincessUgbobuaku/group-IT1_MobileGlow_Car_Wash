package za.ac.cput.repository.user.impl;

import za.ac.cput.domain.user.Login;
import za.ac.cput.repository.user.LoginRepository;

import java.util.HashSet;
import java.util.Set;

public class LoginRepositoryImpl implements LoginRepository {
    private Set<Login> loginDB;
    private static LoginRepository loginRepository = null;

    LoginRepositoryImpl() {loginDB = new HashSet<Login>();}
    public static LoginRepository getLoginRepository() {
        if (loginRepository == null) {
            loginRepository = new LoginRepositoryImpl();
        }
        return loginRepository;
    }

    @Override
    public Login create(Login login) {
        this.loginDB.add(login);
        return login;
    }

    @Override
    public Login read(String loginId) {
        for (Login login : loginDB) {
            if (login.getLoginID().equals(loginId)) {
                return login;
            }
        }
        return null;
    }


    @Override
    public Login update(Login login) {
        Login oldLogin = this.read(login.getLoginID());
        if (oldLogin != null) {
            this.loginDB.remove(oldLogin);
            this.loginDB.add(login);
        }
        return login;
    }

    @Override
    public void delete(String loginId) {
        Login oldLogin = this.read(loginId);
        if (oldLogin != null) {
            this.loginDB.remove(oldLogin);
        }

    }

    @Override
    public Set<Login> getLogins() {
        return this.loginDB;
    }
}
