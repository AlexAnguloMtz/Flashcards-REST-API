package com.aram.auth.service;

import com.aram.auth.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return toUserDetails(findByUsernameOrEmail(usernameOrEmail));
    }

    private UserDetails toUserDetails(AppUser user) {
        return new UserDetailsImpl(user);
    }

    private AppUser findByUsernameOrEmail(String usernameOrEmail) {
        return userService.findByUsernameOrEmail(usernameOrEmail);
    }
}
