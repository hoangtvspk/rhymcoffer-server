package bui.dev.rhymcaffer.service;

import bui.dev.rhymcaffer.dto.user.UserRequest;
import bui.dev.rhymcaffer.dto.user.UserUpdateRequest;
import bui.dev.rhymcaffer.dto.common.BaseResponse;
import bui.dev.rhymcaffer.dto.user.UserResponse;
import bui.dev.rhymcaffer.model.User;
import bui.dev.rhymcaffer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Transactional
        public BaseResponse<Void> createUser(UserRequest request) {
                try {
                        if (userRepository.existsByUsername(request.getUsername())) {
                                return BaseResponse.<Void>builder()
                                                .statusCode(400)
                                                .isSuccess(false)
                                                .message("Username already exists")
                                                .build();
                        }
                        if (userRepository.existsByEmail(request.getEmail())) {
                                return BaseResponse.<Void>builder()
                                                .statusCode(400)
                                                .isSuccess(false)
                                                .message("Email already exists")
                                                .build();
                        }

                        User user = User.builder()
                                        .username(request.getUsername())
                                        .email(request.getEmail())
                                        .password(passwordEncoder.encode(request.getPassword()))
                                        .displayName(request.getDisplayName())
                                        .bio(request.getBio())
                                        .imageUrl(request.getImageUrl())
                                        .build();

                        userRepository.save(user);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("User created successfully")
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<UserResponse> getUser(Long id) {
                try {
                        User user = userRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("User not found"));
                        UserResponse response = mapToResponse(user);
                        return BaseResponse.<UserResponse>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(response)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<UserResponse>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<UserResponse> getUserByUsername(String username) {
                try {
                        User user = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new RuntimeException("User not found"));
                        UserResponse response = mapToResponse(user);
                        return BaseResponse.<UserResponse>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(response)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<UserResponse>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<UserResponse>> getAllUsers() {
                try {
                        List<User> users = userRepository.findAll();
                        List<UserResponse> responses = users.stream()
                                        .map(this::mapToResponse)
                                        .toList();
                        return BaseResponse.<List<UserResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<UserResponse>>builder()
                                        .statusCode(500)
                                        .isSuccess(false)
                                        .message("Failed to retrieve users: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<UserResponse>> searchUsers(String query) {
                try {
                        List<User> users = userRepository
                                        .findByUsernameContainingIgnoreCaseOrDisplayNameContainingIgnoreCase(
                                                        query, query);
                        List<UserResponse> responses = users.stream()
                                        .map(this::mapToResponse)
                                        .toList();
                        return BaseResponse.<List<UserResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<UserResponse>>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message("Search failed: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional
        public BaseResponse<Void> updateUser(Long id, UserUpdateRequest request) {
                try {
                        User user = userRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
                                if (userRepository.existsByUsername(request.getUsername())) {
                                        return BaseResponse.<Void>builder()
                                                        .statusCode(400)
                                                        .isSuccess(false)
                                                        .message("Username already exists")
                                                        .build();
                                }
                                user.setUsername(request.getUsername());
                        }

                        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
                                if (userRepository.existsByEmail(request.getEmail())) {
                                        return BaseResponse.<Void>builder()
                                                        .statusCode(400)
                                                        .isSuccess(false)
                                                        .message("Email already exists")
                                                        .build();
                                }
                                user.setEmail(request.getEmail());
                        }

                        if (request.getPassword() != null) {
                                user.setPassword(passwordEncoder.encode(request.getPassword()));
                        }
                        if (request.getDisplayName() != null) {
                                user.setDisplayName(request.getDisplayName());
                        }
                        if (request.getBio() != null) {
                                user.setBio(request.getBio());
                        }
                        if (request.getImageUrl() != null) {
                                user.setImageUrl(request.getImageUrl());
                        }

                        userRepository.save(user);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("User updated successfully")
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional
        public BaseResponse<Void> followUser(Long userId, Long followerId) {
                try {
                        User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new RuntimeException("User not found"));
                        User follower = userRepository.findById(followerId)
                                        .orElseThrow(() -> new RuntimeException("Follower not found"));

                        if (user.getFollowers().contains(follower)) {
                                return BaseResponse.<Void>builder()
                                                .statusCode(400)
                                                .isSuccess(false)
                                                .message("Already following this user")
                                                .build();
                        }

                        user.getFollowers().add(follower);
                        userRepository.save(user);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("User followed successfully")
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional
        public BaseResponse<Void> unfollowUser(Long userId, Long followerId) {
                try {
                        User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new RuntimeException("User not found"));
                        User follower = userRepository.findById(followerId)
                                        .orElseThrow(() -> new RuntimeException("Follower not found"));

                        if (!user.getFollowers().contains(follower)) {
                                return BaseResponse.<Void>builder()
                                                .statusCode(400)
                                                .isSuccess(false)
                                                .message("Not following this user")
                                                .build();
                        }

                        user.getFollowers().remove(follower);
                        userRepository.save(user);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("User unfollowed successfully")
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<UserResponse>> getFollowers(Long userId) {
                try {
                        User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new RuntimeException("User not found"));
                        List<UserResponse> responses = user.getFollowers().stream()
                                        .map(this::mapToResponse)
                                        .toList();
                        return BaseResponse.<List<UserResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<List<UserResponse>>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<UserResponse>> getFollowing(Long userId) {
                try {
                        User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new RuntimeException("User not found"));
                        List<UserResponse> responses = user.getFollowing().stream()
                                        .map(this::mapToResponse)
                                        .toList();
                        return BaseResponse.<List<UserResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<List<UserResponse>>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional
        public BaseResponse<Void> deleteUser(Long id) {
                try {
                        if (!userRepository.existsById(id)) {
                                return BaseResponse.<Void>builder()
                                                .statusCode(404)
                                                .isSuccess(false)
                                                .message("User not found")
                                                .build();
                        }
                        userRepository.deleteById(id);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("User deleted successfully")
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(500)
                                        .isSuccess(false)
                                        .message("Failed to delete user: " + e.getMessage())
                                        .build();
                }
        }

        private UserResponse mapToResponse(User user) {
                return UserResponse.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .email(user.getEmail())
                                .displayName(user.getDisplayName())
                                .bio(user.getBio())
                                .imageUrl(user.getImageUrl())
                                .playlistIds(user.getPlaylists().stream()
                                                .map(playlist -> playlist.getId())
                                                .collect(Collectors.toSet()))
                                .savedTrackIds(user.getSavedTracks().stream()
                                                .map(track -> track.getId())
                                                .collect(Collectors.toSet()))
                                .savedAlbumIds(user.getSavedAlbums().stream()
                                                .map(album -> album.getId())
                                                .collect(Collectors.toSet()))
                                .followedArtistIds(user.getFollowedArtists().stream()
                                                .map(artist -> artist.getId())
                                                .collect(Collectors.toSet()))
                                .followerIds(user.getFollowers().stream()
                                                .map(follower -> follower.getId())
                                                .collect(Collectors.toSet()))
                                .followingIds(user.getFollowing().stream()
                                                .map(following -> following.getId())
                                                .collect(Collectors.toSet()))
                                .createdAt(user.getCreatedAt())
                                .updatedAt(user.getUpdatedAt())
                                .build();
        }
}