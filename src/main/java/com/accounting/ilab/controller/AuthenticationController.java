package com.accounting.ilab.controller;


import com.accounting.ilab.model.request.LoginRequestDto;
import com.accounting.ilab.model.request.RegisterRequestDto;
import com.accounting.ilab.model.response.JwtAuthenticationResponse;
import com.accounting.ilab.model.response.base.ApiResponse;
import com.accounting.ilab.service.AuthenticationService;
import com.accounting.ilab.util.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/v1/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<JwtAuthenticationResponse> register(@Valid @RequestBody RegisterRequestDto request) {
        return ApiResponseUtil.generateGenericResponse(authenticationService.register(request));
    }

    @PostMapping("v1/login")
    public ApiResponse<JwtAuthenticationResponse> login(@Valid @RequestBody LoginRequestDto request) {
        return ApiResponseUtil.generateGenericResponse(authenticationService.login(request));
    }


}
