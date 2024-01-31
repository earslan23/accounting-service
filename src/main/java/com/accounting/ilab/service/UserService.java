package com.accounting.ilab.service;

import com.accounting.ilab.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    UserDetailsService userDetailsService();

    User getReferenceById(final Long id);

    User getCurrentUser();

}
