package dev.curly.pp_3_1_4.service;

import dev.curly.pp_3_1_4.model.Role;
import dev.curly.pp_3_1_4.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAll();

    void add(User user);

    User getById(long id);

    void update(User user);

    void delete(long id);

    boolean isUserWithRoleExists(Role role);
}
