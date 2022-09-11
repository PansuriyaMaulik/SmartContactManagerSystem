//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.smart.config;

import com.smart.Repository.UserRepository;
import com.smart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public UserDetailsServiceImpl() {
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByUserName = this.userRepository.getUserByUserName(username);
        if (userByUserName == null) {
            throw new UsernameNotFoundException("Could not found user !!");
        } else {
            CustomUserDetails customUserDetails = new CustomUserDetails(userByUserName);
            return customUserDetails;
        }
    }
}
