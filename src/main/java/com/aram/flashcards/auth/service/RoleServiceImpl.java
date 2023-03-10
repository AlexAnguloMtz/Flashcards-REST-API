package com.aram.flashcards.auth.service;


import com.aram.flashcards.common.exception.NotFoundException;
import com.aram.flashcards.auth.model.AppRole;
import com.aram.flashcards.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public AppRole findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Could not find role with name = %s".formatted(name)));
    }
}
