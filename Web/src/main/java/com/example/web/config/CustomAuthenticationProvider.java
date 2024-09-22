package com.example.web.config;

import com.example.web.Request.LoginRequest;
import com.example.web.dto.UserDTO;
import com.example.web.repository.UserFeignClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
@Slf4j

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserFeignClient userFeignClient;

    @Autowired
    public CustomAuthenticationProvider(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = (String) authentication.getCredentials();
//
//
//        try {
//            LoginRequest loginRequest = new LoginRequest(username, password); // Создаем объект LoginRequest
//            ResponseEntity<UserDTO> response = userFeignClient.login(loginRequest);
//
//            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                UserDTO userDTO = response.getBody();
//                String storedPassword = userDTO.getPassword();
//                if (storedPassword == null) {
//                    throw new BadCredentialsException("Password is missing in user details");
//                }
//
//                // Если пароли хранятся в зашифрованном виде
//                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//                boolean passwordMatches = passwordEncoder.matches(password, storedPassword);
//
//                if (passwordMatches) {
//                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userDTO.getRole());
//                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(authority);
//
//                    UserDetails userDetails = org.springframework.security.core.userdetails.User
//                            .withUsername(userDTO.getUsername())
//                            .password(storedPassword) // Если пароль зашифрован, используйте зашифрованный
//                            .authorities(authorities)
//                            .build();
//
//                    return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
//                } else {
//                    throw new BadCredentialsException("Invalid credentials");
//                }
//            } else {
//                throw new BadCredentialsException("User details are missing or login failed");
//            }
//        } catch (FeignException e) {
//            throw new BadCredentialsException("Authentication failed", e);
//        }
//    }
@Override
public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = (String) authentication.getCredentials();

    try {
        LoginRequest loginRequest = new LoginRequest(username, password);
        UserDTO userDTO = userFeignClient.login(loginRequest);
        log.info("UserDTO received: {}", userDTO);

        if (userDTO != null) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userDTO.getRole());
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(authority);

            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(userDTO.getUsername())
                    .password("") // Пустой пароль, так как аутентификация уже выполнена
                    .authorities(authorities)
                    .build();

            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Authentication failed");
        }
    } catch (FeignException e) {
        throw new BadCredentialsException("Authentication failed", e);
    }
}
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}