package com.aram.flashcards.auth.data;

import com.aram.flashcards.auth.model.AppRole;
import com.aram.flashcards.auth.repository.RoleRepository;
import com.aram.flashcards.common.data.JsonLoader;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RolesLoader implements CommandLineRunner {

    @Value("${json.roles}")
    private String path;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        roles(path).forEach(roleRepository::save);
    }

    private List<AppRole> roles(String path) {
        return new JsonLoader().readJson(path, new TypeReference<>(){});
    }

}