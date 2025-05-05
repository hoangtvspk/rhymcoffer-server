package bui.dev.rhymcaffer.controller;

import bui.dev.rhymcaffer.dto.request.UserRequest;
import bui.dev.rhymcaffer.dto.request.UserUpdateRequest;
import bui.dev.rhymcaffer.dto.response.BaseResponse;
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
        public ResponseEntity<BaseResponse<Void>> createUser(@RequestBody UserRequest request) {
                return ResponseEntity.ok(userService.createUser(request));
        }

        @GetMapping("/{id}")
        public ResponseEntity<BaseResponse<UserResponse>> getUser(@PathVariable Long id) {
                return ResponseEntity.ok(userService.getUser(id));
        }

        @GetMapping("/username/{username}")
        public ResponseEntity<BaseResponse<UserResponse>> getUserByUsername(@PathVariable String username) {
                return ResponseEntity.ok(userService.getUserByUsername(username));
        }

        @GetMapping
        public ResponseEntity<BaseResponse<List<UserResponse>>> getAllUsers() {
                return ResponseEntity.ok(userService.getAllUsers());
        }

        @GetMapping("/search")
        public ResponseEntity<BaseResponse<List<UserResponse>>> searchUsers(@RequestParam String query) {
                return ResponseEntity.ok(userService.searchUsers(query));
        }

        @PutMapping("/{id}")
        public ResponseEntity<BaseResponse<Void>> updateUser(
                        @PathVariable Long id,
                        @RequestBody UserUpdateRequest request) {
                return ResponseEntity.ok(userService.updateUser(id, request));
        }

        @PostMapping("/{userId}/follow")
        public ResponseEntity<BaseResponse<Void>> followUser(
                        @PathVariable Long userId,
                        Authentication authentication) {
                Long followerId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
                return ResponseEntity.ok(userService.followUser(userId, followerId));
        }

        @PostMapping("/{userId}/unfollow")
        public ResponseEntity<BaseResponse<Void>> unfollowUser(
                        @PathVariable Long userId,
                        Authentication authentication) {
                Long followerId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
                return ResponseEntity.ok(userService.unfollowUser(userId, followerId));
        }

        @GetMapping("/{userId}/followers")
        public ResponseEntity<BaseResponse<List<UserResponse>>> getFollowers(@PathVariable Long userId) {
                return ResponseEntity.ok(userService.getFollowers(userId));
        }

        @GetMapping("/{userId}/following")
        public ResponseEntity<BaseResponse<List<UserResponse>>> getFollowing(@PathVariable Long userId) {
                return ResponseEntity.ok(userService.getFollowing(userId));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<BaseResponse<Void>> deleteUser(@PathVariable Long id) {
                return ResponseEntity.ok(userService.deleteUser(id));
        }
}