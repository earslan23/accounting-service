package com.accounting.ilab.service;

import com.accounting.ilab.model.request.LoginRequestDto;
import com.accounting.ilab.model.request.RegisterRequestDto;
import com.accounting.ilab.model.response.JwtAuthenticationResponse;

public interface AuthenticationService {

    JwtAuthenticationResponse register(RegisterRequestDto request);

    JwtAuthenticationResponse login(LoginRequestDto request);

}
