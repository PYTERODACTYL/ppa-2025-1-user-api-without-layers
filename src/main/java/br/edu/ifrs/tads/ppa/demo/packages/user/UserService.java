package br.edu.ifrs.tads.ppa.demo.packages.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.ifrs.tads.ppa.demo.packages.profile.Profile;
import br.edu.ifrs.tads.ppa.demo.packages.profile.Profile.AccountType;
import br.edu.ifrs.tads.ppa.demo.packages.role.Role;
import br.edu.ifrs.tads.ppa.demo.packages.role.RoleRepository;
import br.edu.ifrs.tads.ppa.demo.packages.user.newUser.NewUser;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Set<String> defaultRoles;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            @Value("${app.user.default.roles}") Set<String> defaultRoles) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.defaultRoles = defaultRoles;
    }

    public void createUser(NewUser newUser) {
        validateNewUser(newUser);

        User user = new User();
        user.setEmail(newUser.email());
        user.setHandle(newUser.handle() != null ? newUser.handle() : generateHandle(newUser.email()));
        user.setPassword(passwordEncoder.encode(newUser.password()));
        
        Set<Role> roles = new HashSet<>(roleRepository.findByNameIn(defaultRoles));

        if (newUser.roles() != null && !newUser.roles().isEmpty()) {
            Set<Role> additionalRoles = roleRepository.findByNameIn(newUser.roles());
            if (additionalRoles.size() != newUser.roles().size()) {
                throw new IllegalArgumentException("Alguns papéis não existem");
            }
            roles.addAll(additionalRoles);
        }

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("O usuário deve ter pelo menos um papel");
        }

        user.setRoles(roles);

        Profile profile = new Profile();
        profile.setName(newUser.name());
        profile.setCompany(newUser.company());
        profile.setType(newUser.type() != null ? newUser.type() : AccountType.FREE);

        profile.setUser(user);
        user.setProfile(profile);

        userRepository.save(user);
    }

    private void validateNewUser(NewUser newUser) {
        if (newUser.email() == null || newUser.password() == null) {
            throw new IllegalArgumentException("Email e senha são obrigatórios");
        }
        if (newUser.email().isEmpty() || newUser.password().isEmpty()) {
            throw new IllegalArgumentException("Email e senha não podem estar vazios");
        }
        if (!newUser.email().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email não é válido");
        }
        if (!newUser.password().matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$")) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 8 caracteres e conter uma letra e um número");
        }
        userRepository.findByEmail(newUser.email()).ifPresent(user -> {
            throw new IllegalArgumentException("Usuário com o email " + newUser.email() + " já existe");
        });
        if (newUser.handle() != null) {
            userRepository.findByHandle(newUser.handle()).ifPresent(user -> {
                throw new IllegalArgumentException("Usuário com o nome " + newUser.handle() + " já existe");
            });
        }
    }

    private String generateHandle(String email) {
        String[] parts = email.split("@");
        String handle = parts[0];
        int i = 1;
        while (userRepository.existsByHandle(handle)) {
            handle = parts[0] + i++;
        }
        return handle;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
