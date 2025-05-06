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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        @Value("${jwt.refresh-expiration}")
        private long refreshExpiration;

        private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
                log.info("Setting refresh token cookie with token: {}", refreshToken);

                Cookie cookie = new Cookie("refreshToken", refreshToken);
                cookie.setHttpOnly(true);
                // cookie.setSecure(true); // Disable for localhost
                cookie.setPath("/");
                cookie.setMaxAge((int) (refreshExpiration / 1000));
                cookie.setAttribute("SameSite", "Lax"); // Use Lax for localhost
                response.addCookie(cookie);

                String cookieHeader = String.format(
                                "refreshToken=%s; HttpOnly; Path=/; Max-Age=%d; SameSite=Lax",
                                refreshToken, (int) (refreshExpiration / 1000));
                response.setHeader("Set-Cookie", cookieHeader);

                log.info("Cookie header set: {}", cookieHeader);
        }

        public BaseResponse<AuthenticationResponse> register(RegisterRequest request, HttpServletResponse response) {
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
                        String accessToken = jwtService.generateToken(userDetails);
                        String refreshToken = jwtService.generateRefreshToken(userDetails);

                        // Set refresh token in cookie
                        setRefreshTokenCookie(response, refreshToken);

                        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                                        .accessToken(accessToken)
                                        .username(user.getUsername())
                                        .displayName(user.getDisplayName())
                                        .email(user.getEmail())
                                        .build();

                        return BaseResponse.<AuthenticationResponse>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("User registered successfully")
                                        .data(authResponse)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<AuthenticationResponse>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        public BaseResponse<AuthenticationResponse> authenticate(AuthenticationRequest request,
                        HttpServletResponse response) {
                try {
                        authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        request.getUsername(),
                                                        request.getPassword()));

                        User user = userRepository.findByUsername(request.getUsername())
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
                        String accessToken = jwtService.generateToken(userDetails);
                        String refreshToken = jwtService.generateRefreshToken(userDetails);

                        // Set refresh token in cookie
                        setRefreshTokenCookie(response, refreshToken);

                        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                                        .accessToken(accessToken)
                                        .username(user.getUsername())
                                        .displayName(user.getDisplayName())
                                        .email(user.getEmail())
                                        .build();

                        return BaseResponse.<AuthenticationResponse>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Login successful")
                                        .data(authResponse)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<AuthenticationResponse>builder()
                                        .statusCode(401)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        public BaseResponse<Void> logout(String token, HttpServletResponse response) {
                try {
                        jwtService.invalidateToken(token);

                        // Clear refresh token cookie
                        Cookie cookie = new Cookie("refreshToken", null);
                        cookie.setHttpOnly(true);
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);

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

        public BaseResponse<AuthenticationResponse> refreshToken(String refreshToken, HttpServletResponse response) {
                try {
                        String username = jwtService.extractUsername(refreshToken);
                        User user = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

                        if (!jwtService.isRefreshTokenValid(refreshToken, userDetails)) {
                                throw new RuntimeException("Invalid refresh token");
                        }

                        String newAccessToken = jwtService.generateToken(userDetails);
                        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

                        // Set new refresh token in cookie
                        setRefreshTokenCookie(response, newRefreshToken);

                        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                                        .accessToken(newAccessToken)
                                        .username(user.getUsername())
                                        .displayName(user.getDisplayName())
                                        .email(user.getEmail())
                                        .build();

                        return BaseResponse.<AuthenticationResponse>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Token refreshed successfully")
                                        .data(authResponse)
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