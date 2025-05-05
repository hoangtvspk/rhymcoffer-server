package bui.dev.rhymcaffer.service;

import bui.dev.rhymcaffer.dto.request.AuthenticationRequest;
import bui.dev.rhymcaffer.dto.request.RegisterRequest;
import bui.dev.rhymcaffer.dto.response.AuthenticationResponse;
import bui.dev.rhymcaffer.dto.response.BaseResponse;
import bui.dev.rhymcaffer.model.Role;
import bui.dev.rhymcaffer.model.User;
import bui.dev.rhymcaffer.repository.RoleRepository;
import bui.dev.rhymcaffer.repository.UserRepository;
import bui.dev.rhymcaffer.security.JwtService;
import bui.dev.rhymcaffer.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public BaseResponse<AuthenticationResponse> register(RegisterRequest request) {
                try {
                        if (userRepository.existsByUsername(request.getUsername())) {
                                return BaseResponse.<AuthenticationResponse>builder()
                                                .statusCode(400)
                                                .isSuccess(false)
                                                .message("Username is already taken")
                                                .build();
                        }

                        if (userRepository.existsByEmail(request.getEmail())) {
                                return BaseResponse.<AuthenticationResponse>builder()
                                                .statusCode(400)
                                                .isSuccess(false)
                                                .message("Email is already in use")
                                                .build();
                        }

                        // Get or create the default ROLE_USER role
                        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                                        .orElseGet(() -> roleRepository.save(Role.builder()
                                                        .name(Role.RoleName.ROLE_USER)
                                                        .build()));

                        User user = User.builder()
                                        .username(request.getUsername())
                                        .email(request.getEmail())
                                        .password(passwordEncoder.encode(request.getPassword()))
                                        .displayName(request.getDisplayName())
                                        .country(request.getCountry())
                                        .roles(new HashSet<>(Set.of(userRole)))
                                        .build();

                        userRepository.save(user);

                        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
                        String jwtToken = jwtService.generateToken(userDetails);

                        AuthenticationResponse response = AuthenticationResponse.builder()
                                        .token(jwtToken)
                                        .username(user.getUsername())
                                        .displayName(user.getDisplayName())
                                        .email(user.getEmail())
                                        .build();

                        return BaseResponse.<AuthenticationResponse>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("User registered successfully")
                                        .data(response)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<AuthenticationResponse>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        public BaseResponse<AuthenticationResponse> authenticate(AuthenticationRequest request) {
                try {
                        authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        request.getUsername(),
                                                        request.getPassword()));

                        User user = userRepository.findByUsername(request.getUsername())
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
                        String jwtToken = jwtService.generateToken(userDetails);

                        AuthenticationResponse response = AuthenticationResponse.builder()
                                        .token(jwtToken)
                                        .username(user.getUsername())
                                        .displayName(user.getDisplayName())
                                        .email(user.getEmail())
                                        .build();

                        return BaseResponse.<AuthenticationResponse>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Login successful")
                                        .data(response)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<AuthenticationResponse>builder()
                                        .statusCode(401)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        public BaseResponse<Void> logout(String token) {
                try {
                        jwtService.invalidateToken(token);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Logout successful")
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        public BaseResponse<AuthenticationResponse> refreshToken(String token) {
                try {
                        String username = jwtService.extractUsername(token);
                        User user = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
                        String newToken = jwtService.generateToken(userDetails);

                        AuthenticationResponse response = AuthenticationResponse.builder()
                                        .token(newToken)
                                        .username(user.getUsername())
                                        .displayName(user.getDisplayName())
                                        .email(user.getEmail())
                                        .build();

                        return BaseResponse.<AuthenticationResponse>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Token refreshed successfully")
                                        .data(response)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<AuthenticationResponse>builder()
                                        .statusCode(401)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }
}