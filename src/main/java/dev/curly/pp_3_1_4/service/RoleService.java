package dev.curly.pp_3_1_4.service;

import dev.curly.pp_3_1_4.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAll();

    void add(Role role);
}
