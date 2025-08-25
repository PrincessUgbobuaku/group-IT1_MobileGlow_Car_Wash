package za.ac.cput.controller.user;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.user.Login;
import za.ac.cput.service.user.implementation.LoginServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/Login")
public class LoginController {

    private final LoginServiceImpl loginService;

    @Autowired
    public LoginController(LoginServiceImpl loginService) {
        this.loginService = loginService;
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

    @DeleteMapping("/delete/{Id}")
    public void delete(@PathVariable Long Id) {
        loginService.delete(Id);
    }

}
