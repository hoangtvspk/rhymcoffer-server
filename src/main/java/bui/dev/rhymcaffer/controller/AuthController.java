package bui.dev.rhymcaffer.controller;

import bui.dev.rhymcaffer.dto.request.AuthenticationRequest;
import bui.dev.rhymcaffer.dto.request.RegisterRequest;
import bui.dev.rhymcaffer.dto.response.AuthenticationResponse;
import bui.dev.rhymcaffer.dto.response.BaseResponse;
import bui.dev.rhymcaffer.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<AuthenticationResponse>> register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response) {
        log.info("Registering new user: {}", request.getUsername());
        return ResponseEntity.ok(authenticationService.register(request, response));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthenticationResponse>> login(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response) {
        log.info("Logging in user: {}", request.getUsername());
        return ResponseEntity.ok(authenticationService.authenticate(request, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout(
            @RequestHeader("Authorization") String token,
            HttpServletResponse response) {
        log.info("Logging out user");
        return ResponseEntity.ok(authenticationService.logout(token, response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse<AuthenticationResponse>> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        log.info("Attempting to refresh token");

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            log.info("Found {} cookies", cookies.length);
            for (Cookie cookie : cookies) {
                log.info("Cookie: {} = {}", cookie.getName(), cookie.getValue());
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    log.info("Found refresh token in cookie");
                    break;
                }
            }
        } else {
            log.info("No cookies found in request");
        }

        if (refreshToken == null) {
            log.warn("Refresh token not found in cookies");
            return ResponseEntity.badRequest().body(
                    BaseResponse.<AuthenticationResponse>builder()
                            .statusCode(400)
                            .isSuccess(false)
                            .message("Refresh token not found")
                            .build());
        }

        return ResponseEntity.ok(authenticationService.refreshToken(refreshToken, response));
    }
}