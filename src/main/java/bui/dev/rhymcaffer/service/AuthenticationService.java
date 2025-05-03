package bui.dev.rhymcaffer.service;

import bui.dev.rhymcaffer.dto.request.AuthenticationRequest;
import bui.dev.rhymcaffer.dto.request.RegisterRequest;
import bui.dev.rhymcaffer.dto.response.AuthenticationResponse;
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

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use");
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

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        String jwtToken = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .build();
    }
} 