package com.aram.flashcards.auth.service;

import com.aram.flashcards.auth.model.AppRole;
import com.aram.flashcards.auth.model.AppUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
class UserDetailsImpl implements CustomUserDetails {

    private final AppUser user;

    @Override
    public String getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(toGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private GrantedAuthority toGrantedAuthority(AppRole appRole) {
        return new SimpleGrantedAuthority(appRole.getName());
    }

}
