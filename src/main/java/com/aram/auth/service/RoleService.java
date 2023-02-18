package com.aram.auth.service;

import com.aram.auth.model.AppRole;

public interface RoleService {

    AppRole findByName(String name);
}
