package com.portfolio.recipebook.security.auth;

import com.portfolio.recipebook.security.auth.user.User;
import com.portfolio.recipebook.security.auth.user.UserRepository;
import com.portfolio.recipebook.security.auth.user.group.AuthGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RecipeBookUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public RecipeBookUserDetailsService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User user = this.userRepository.findByNickname(nickname).orElseThrow(() ->
                new UsernameNotFoundException("Cannot find nickname: " + nickname));
        List<AuthGroup> authGroups = new ArrayList<>();
        authGroups.add(user.getAuthGroup());
        return new RecipeBookUserPrinciple(user, authGroups);
    }
}
