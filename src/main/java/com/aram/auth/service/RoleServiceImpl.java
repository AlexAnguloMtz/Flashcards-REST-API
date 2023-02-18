package com.aram.auth.service;


import com.aram.auth.exception.NotFoundException;
import com.aram.auth.model.AppRole;
import com.aram.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public AppRole findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(NotFoundException::new);
    }
}
