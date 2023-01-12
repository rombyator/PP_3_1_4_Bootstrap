package dev.curly.pp_3_1_4.init;

import dev.curly.pp_3_1_4.model.Role;
import dev.curly.pp_3_1_4.model.User;
import dev.curly.pp_3_1_4.service.RoleService;
import dev.curly.pp_3_1_4.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbInit {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public DbInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * Add admin user if no admins present in db
     */
    @PostConstruct
    private void initializeDb() {
        if (userService.isUserWithRoleExists(Role.adminRole())) {
            return;
        }

        // Add 2 roles
        var adminRole = Role.adminRole();
        var userRole = Role.userRole();
        roleService.add(adminRole);
        roleService.add(userRole);

        // Add admin
        var admin = new User();
        admin.setName("admin");
        admin.setPassword("admin");
        admin.setEmail("admin@local.test");
        admin.setAge(0);
        admin.setRoles(List.of(adminRole, userRole));
        userService.add(admin);

        // Add user
        var user = new User();
        user.setName("user");
        user.setPassword("user");
        user.setEmail("user@local.test");
        user.setAge(0);
        user.setRoles(List.of(userRole));
        userService.add(user);
    }
}
