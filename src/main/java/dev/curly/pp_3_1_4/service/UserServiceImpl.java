package dev.curly.pp_3_1_4.service;

import dev.curly.pp_3_1_4.exceptions.UserEmailAlreadyInUse;
import dev.curly.pp_3_1_4.model.Role;
import dev.curly.pp_3_1_4.model.User;
import dev.curly.pp_3_1_4.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void add(User user) throws UserEmailAlreadyInUse {
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new UserEmailAlreadyInUse();
        }

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
        var dbUser = getById(user.getId());
        var rawPassword = user.getPassword();

        if (rawPassword != null && !rawPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        } else {
            user.setPassword(dbUser.getPassword());
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
}
