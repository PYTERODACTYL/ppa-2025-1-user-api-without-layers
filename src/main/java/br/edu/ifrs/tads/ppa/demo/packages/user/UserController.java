package br.edu.ifrs.tads.ppa.demo.packages.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrs.tads.ppa.demo.packages.profile.Profile;
import br.edu.ifrs.tads.ppa.demo.packages.profile.Profile.AccountType;
import br.edu.ifrs.tads.ppa.demo.packages.role.Role;
import br.edu.ifrs.tads.ppa.demo.packages.role.RoleRepository;
import br.edu.ifrs.tads.ppa.demo.packages.user.newUser.NewUser;
import br.edu.ifrs.tads.ppa.demo.packages.user.UserService;



@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createUser(@RequestBody NewUser newUser) {
        userService.createUser(newUser);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}