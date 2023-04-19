package com.example.ManageDom.service;


import com.example.ManageDom.config.MyUserDetails;
import com.example.ManageDom.dao.UserResponsitory;
import com.example.ManageDom.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserResponsitory userResponsitory;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userResponsitory.getUserByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("cant found");
        }
        return new MyUserDetails(user);
    }
}
