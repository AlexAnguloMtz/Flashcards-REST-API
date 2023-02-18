package com.aram.auth.data;

import com.aram.auth.model.AppRole;
import com.aram.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RolesLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        roles().forEach(roleRepository::save);
    }

    private List<AppRole> roles() {
        return List.of(
                new AppRole(1, "ROLE_ADMIN"),
                new AppRole(2, "ROLE_USER")
        );
    }

}
