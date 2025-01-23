package org.example.expert.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import org.example.expert.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authority = new ArrayList<>();
        authority.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getUserRole().name();
            }
        });
        return authority;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public String getNickname() {
        return user.getNickname();
    }

    public Long getId(){
        return user.getId();
    }
}
