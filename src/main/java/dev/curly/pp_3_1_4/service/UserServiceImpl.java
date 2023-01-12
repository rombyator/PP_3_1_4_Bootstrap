package dev.curly.pp_3_1_4.service;

import dev.curly.pp_3_1_4.model.Role;
import dev.curly.pp_3_1_4.model.User;
import dev.curly.pp_3_1_4.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Override
    @Transactional
    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Override
    public User getById(long id) {
        return userRepo.getReferenceById(id);
    }

    @Override
    @Transactional
    public void update(User user) {
        var dbUser = loadUserByUsername(user.getUsername());
        var rawPassword = user.getPassword();
        var encodedPassword = dbUser.getPassword();

        if (rawPassword != null && !passwordEncoder.matches(rawPassword, encodedPassword)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepo.save(user);
    }

    @Override
    @Transactional
    public void delete(long id) {
        userRepo.deleteById(id);
    }

    @Override
    public boolean isUserWithRoleExists(Role role) {
        return userRepo.existsWithRole(role.getName());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByName(username)
                       .orElseThrow(
                           () -> new UsernameNotFoundException(
                               String.format("No user with username: %s", username)
                           )
                       );
    }
}
