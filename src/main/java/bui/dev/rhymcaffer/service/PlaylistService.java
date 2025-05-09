package bui.dev.rhymcaffer.service;

import bui.dev.rhymcaffer.dto.playlist.PlaylistRequest;
import bui.dev.rhymcaffer.dto.common.BaseResponse;
import bui.dev.rhymcaffer.dto.playlist.PlaylistResponse;
import bui.dev.rhymcaffer.model.*;
import bui.dev.rhymcaffer.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

        private final PlaylistRepository playlistRepository;
        private final TrackRepository trackRepository;
        private final UserRepository userRepository;

        @Transactional
        public BaseResponse<Void> createPlaylist(PlaylistRequest request, Long ownerId) {
                try {
                        Playlist playlist = Playlist.builder()
                                        .name(request.getName())
                                        .description(request.getDescription())
                                        .imageUrl(request.getImageUrl())
                                        .isPublic(request.getIsPublic())
                                        .collaborative(request.getCollaborative())
                                        .build();

                        User owner = userRepository.findById(ownerId)
                                        .orElseThrow(() -> new RuntimeException("Owner not found"));
                        playlist.setOwner(owner);

                        playlistRepository.save(playlist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Playlist created successfully")
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
        public BaseResponse<PlaylistResponse> getPlaylist(Long id) {
                try {
                        Playlist playlist = playlistRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Playlist not found"));
                        PlaylistResponse response = mapToResponse(playlist);
                        return BaseResponse.<PlaylistResponse>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(response)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<PlaylistResponse>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<PlaylistResponse>> getAllPlaylists() {
                try {
                        List<Playlist> playlists = playlistRepository.findAll();
                        List<PlaylistResponse> responses = playlists.stream()
                                        .map(this::mapToResponse)
                                        .toList();
                        return BaseResponse.<List<PlaylistResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<PlaylistResponse>>builder()
                                        .statusCode(500)
                                        .isSuccess(false)
                                        .message("Failed to retrieve playlists: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<PlaylistResponse>> searchPlaylists(String name) {
                try {
                        List<Playlist> playlists = playlistRepository.findByNameContainingIgnoreCase(name);
                        List<PlaylistResponse> responses = playlists.stream()
                                        .map(this::mapToResponse)
                                        .toList();
                        return BaseResponse.<List<PlaylistResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<PlaylistResponse>>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message("Search failed: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<PlaylistResponse>> getPlaylistsByOwner(Long ownerId) {
                try {
                        List<Playlist> playlists = playlistRepository.findByOwner_Id(ownerId);
                        List<PlaylistResponse> responses = playlists.stream()
                                        .map(this::mapToResponse)
                                        .toList();
                        return BaseResponse.<List<PlaylistResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<List<PlaylistResponse>>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<PlaylistResponse>> getFollowedPlaylists(Long userId) {
                try {
                        List<Playlist> playlists = playlistRepository.findByFollowers_Id(userId);
                        List<PlaylistResponse> responses = playlists.stream()
                                        .map(this::mapToResponse)
                                        .toList();
                        return BaseResponse.<List<PlaylistResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<List<PlaylistResponse>>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<PlaylistResponse>> getPublicPlaylists() {
                try {
                        List<Playlist> playlists = playlistRepository.findByIsPublicTrue();
                        List<PlaylistResponse> responses = playlists.stream()
                                        .map(this::mapToResponse)
                                        .toList();
                        return BaseResponse.<List<PlaylistResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<PlaylistResponse>>builder()
                                        .statusCode(500)
                                        .isSuccess(false)
                                        .message("Failed to retrieve public playlists: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional
        public BaseResponse<Void> addTracksToPlaylist(Long playlistId, List<Long> trackIds) {
                try {
                        Playlist playlist = playlistRepository.findById(playlistId)
                                        .orElseThrow(() -> new RuntimeException("Playlist not found"));
                        List<Track> tracks = trackRepository.findAllById(trackIds);

                        playlist.getTracks().addAll(tracks);
                        playlistRepository.save(playlist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Tracks added to playlist successfully")
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
        public BaseResponse<Void> removeTracksFromPlaylist(Long playlistId, List<Long> trackIds) {
                try {
                        Playlist playlist = playlistRepository.findById(playlistId)
                                        .orElseThrow(() -> new RuntimeException("Playlist not found"));
                        List<Track> tracks = trackRepository.findAllById(trackIds);

                        playlist.getTracks().removeAll(tracks);
                        playlistRepository.save(playlist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Tracks removed from playlist successfully")
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
        public BaseResponse<Void> followPlaylist(Long playlistId, Long userId) {
                try {
                        Playlist playlist = playlistRepository.findById(playlistId)
                                        .orElseThrow(() -> new RuntimeException("Playlist not found"));
                        User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        playlist.getFollowers().add(user);
                        playlistRepository.save(playlist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Playlist followed successfully")
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
        public BaseResponse<Void> unfollowPlaylist(Long playlistId, Long userId) {
                try {
                        Playlist playlist = playlistRepository.findById(playlistId)
                                        .orElseThrow(() -> new RuntimeException("Playlist not found"));
                        User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        playlist.getFollowers().remove(user);
                        playlistRepository.save(playlist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Playlist unfollowed successfully")
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
        public BaseResponse<Void> deletePlaylist(Long id) {
                try {
                        playlistRepository.deleteById(id);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Playlist deleted successfully")
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
        public BaseResponse<Void> updatePlaylist(Long id, PlaylistRequest request, Long ownerId) {
                try {
                        Playlist playlist = playlistRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Playlist not found"));

                        if (request.getName() != null) {
                                playlist.setName(request.getName());
                        }
                        if (request.getDescription() != null) {
                                playlist.setDescription(request.getDescription());
                        }
                        if (request.getImageUrl() != null) {
                                playlist.setImageUrl(request.getImageUrl());
                        }
                        if (request.getIsPublic() != null) {
                                playlist.setIsPublic(request.getIsPublic());
                        }
                        if (request.getCollaborative() != null) {
                                playlist.setCollaborative(request.getCollaborative());
                        }
                        User owner = userRepository.findById(ownerId)
                                        .orElseThrow(() -> new RuntimeException("Owner not found"));
                        playlist.setOwner(owner);

                        playlistRepository.save(playlist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Playlist updated successfully")
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        private PlaylistResponse mapToResponse(Playlist playlist) {
                return PlaylistResponse.builder()
                                .id(playlist.getId())
                                .name(playlist.getName())
                                .description(playlist.getDescription())
                                .imageUrl(playlist.getImageUrl())
                                .isPublic(playlist.getIsPublic())
                                .collaborative(playlist.getCollaborative())
                                .ownerId(playlist.getOwner().getId())
                                .trackIds(playlist.getTracks().stream()
                                                .map(track -> track.getId())
                                                .collect(Collectors.toSet()))
                                .followerIds(playlist.getFollowers().stream()
                                                .map(user -> user.getId())
                                                .collect(Collectors.toSet()))
                                .createdAt(playlist.getCreatedAt())
                                .updatedAt(playlist.getUpdatedAt())
                                .build();
        }
}