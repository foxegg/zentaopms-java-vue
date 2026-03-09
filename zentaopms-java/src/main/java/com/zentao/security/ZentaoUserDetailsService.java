package com.zentao.security;

import com.zentao.entity.User;
import com.zentao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ZentaoUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByAccountAndDeleted(username, 0)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
        return new UserPrincipal(user);
    }
}
