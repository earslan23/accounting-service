package com.accounting.ilab.service.impl;

import com.accounting.ilab.entity.User;
import com.accounting.ilab.exception.BusinessException;
import com.accounting.ilab.repos.UserRepository;
import com.accounting.ilab.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByUserName(email)
                .orElseThrow(() -> new BusinessException("User not found"));
    }

    @Override
    public User getReferenceById(final Long id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
