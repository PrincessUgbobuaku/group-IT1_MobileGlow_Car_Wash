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




    @GetMapping("/byUser/{userId}")
    public ResponseEntity<?> getLoginByUserId(@PathVariable Long userId) {
        Login login = loginService.findLoginByUserId(userId);
        if (login == null) {
            return ResponseEntity.status(404).body("Login not found for user ID: " + userId);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("loginId", login.getLoginID());
        response.put("emailAddress", login.getEmailAddress());
        return ResponseEntity.ok(response);
    }






    @GetMapping("/{loginId}")
    public ResponseEntity<?> getLoginEmail(@PathVariable Long loginId) {
        Login login = loginService.read(loginId);
        if (login == null) {
            return ResponseEntity.status(404).body("Login not found for ID: " + loginId);
        }

        Map<String, String> response = new HashMap<>();
        response.put("emailAddress", login.getEmailAddress());
        return ResponseEntity.ok(response);
    } //inspect

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
            response.put("user_id", user != null ? user.getUserId() : null);

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

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        if (request.getEmail() == null || request.getCurrentPassword() == null || request.getNewPassword() == null) {
            return ResponseEntity.badRequest().body("{\"message\":\"Email, current password, and new password are required\"}");
        }

        boolean success = loginService.changePassword(request.getEmail(), request.getCurrentPassword(), request.getNewPassword());
        if (success) {
            return ResponseEntity.ok("{\"message\":\"Password changed successfully\"}");
        } else {
            return ResponseEntity.status(400).body("{\"message\":\"Failed to change password. Check current password or user existence.\"}");
        }
    }

    // DTO for change password request
    public static class ChangePasswordRequest {
        private String email;
        private String currentPassword;
        private String newPassword;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

}
