package bui.dev.rhymcaffer.controller;

import bui.dev.rhymcaffer.dto.request.UserRequest;
import bui.dev.rhymcaffer.dto.response.UserResponse;
import bui.dev.rhymcaffer.security.UserDetailsImpl;
import bui.dev.rhymcaffer.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest request
    ) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(
            @RequestParam String query
    ) {
        return ResponseEntity.ok(userService.searchUsers(query));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request,
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        if (!id.equals(userId)) {
            throw new RuntimeException("You can only update your own profile");
        }
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<Void> followUser(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        userService.followUser(userId, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unfollow")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        userService.unfollowUser(userId, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<List<UserResponse>> getFollowers(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.getFollowers(id));
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<List<UserResponse>> getFollowing(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.getFollowing(id));
    }
} 