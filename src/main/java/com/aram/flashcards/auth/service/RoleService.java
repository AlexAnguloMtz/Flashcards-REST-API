package com.aram.flashcards.auth.service;

import com.aram.flashcards.auth.model.AppRole;

public interface RoleService {

    AppRole findByName(String name);
}
