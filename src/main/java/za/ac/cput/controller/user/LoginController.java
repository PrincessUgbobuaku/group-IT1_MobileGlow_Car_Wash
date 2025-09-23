package za.ac.cput.controller.user;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;
import za.ac.cput.service.user.JWTService;
import za.ac.cput.service.user.LoginService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Login")
@CrossOrigin(
        origins = "http://localhost:3000",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class LoginController {

    private final LoginService loginService;
    private final JWTService jwtService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
        this.jwtService = new JWTService();
    }

    @PostMapping("/create")
    public ResponseEntity<Login> create(@RequestBody Login login) {
        Login createdLogin = loginService.create(login);
        return ResponseEntity.ok(createdLogin);
    }

    @GetMapping("/read/{Id}")
    public ResponseEntity<Login> read(@PathVariable Long Id) {
        Login login = loginService.read(Id);
        if (login == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(login);
    }

    @PutMapping("/update")
    public ResponseEntity<Login> update(@RequestBody Login login) {
        if (login.getLoginID() == null) {
            return ResponseEntity.badRequest().build();
        }
        Login updatedLogin = loginService.update(login);
        if (updatedLogin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedLogin);
    }

    @GetMapping("/getAllLogins")
    public ResponseEntity<List<Login>> getAllLogins() {
        List<Login> logins = loginService.getAllLogins();
        if (logins == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(logins);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Login login) {
        if (login.getEmailAddress() == null || login.getPassword() == null) {
            return ResponseEntity.badRequest().body("{\"message\":\"Email and password are required\"}");
        }

        //find login in DB.
        Login foundLogin = loginService.findByEmailAddress(login.getEmailAddress());

        if (foundLogin != null && loginService.checkPassword(login.getPassword(), foundLogin.getPassword())) {

            //Get the associated user and their role.
            User user = loginService.findUserByLogin(foundLogin);

            String roleDescription = (user != null && user.getRoleDescription() != null)
                    ? user.getRoleDescription().name()
                    : "CLIENT";

            // Generate JWT token
            String jwtToken = jwtService.generateToken(foundLogin.getEmailAddress());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("role_description", roleDescription);
            response.put("token", jwtToken);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("{\"message\":\"Invalid email or password\"}");
        }
    }


    @DeleteMapping("/delete/{Id}")
    public boolean delete(@PathVariable Long Id) {
        loginService.delete(Id);
        return true;
    }

}
