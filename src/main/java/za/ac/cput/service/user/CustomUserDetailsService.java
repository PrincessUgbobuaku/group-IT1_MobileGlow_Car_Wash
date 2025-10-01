package za.ac.cput.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;
import za.ac.cput.repository.user.ILoginRepository;
import za.ac.cput.repository.user.IUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;
    private final ILoginRepository loginRepository;

    @Autowired
    public CustomUserDetailsService(IUserRepository userRepository, ILoginRepository loginRepository) {
        this.userRepository = userRepository;
        this.loginRepository = loginRepository;
    }

    //This method checks emailAddress from the login table and use that emailAddress to find the role of the user.
    //in the user table using that oneToOne relationship. So that we can be able to access different dashboards using roles.
    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        //Find Login by email.
        Login login = loginRepository.findByEmailAddress(emailAddress);
        if (login == null) {
            throw new UsernameNotFoundException("User not found with emailAddress: " + emailAddress);
        }

        //Get associated User (to access role).
        User user = userRepository.findByLogin_EmailAddress(emailAddress);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with emailAddress: " + emailAddress);
        }

        String role = user.getRoleDescription().name(); //CLIENT or EMPLOYEE
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getLogin().getEmailAddress())
                .password(user.getLogin().getPassword()) //hashed password.
                .roles(role) //dynamic role.
                .build();
    }
}
