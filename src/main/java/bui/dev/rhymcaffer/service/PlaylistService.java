package bui.dev.rhymcaffer.service;

import bui.dev.rhymcaffer.dto.request.PlaylistRequest;
import bui.dev.rhymcaffer.dto.response.PlaylistResponse;
import bui.dev.rhymcaffer.model.*;
import bui.dev.rhymcaffer.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;

    @Transactional
    public PlaylistResponse createPlaylist(PlaylistRequest request, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Playlist playlist = Playlist.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : true)
                .collaborative(request.getCollaborative() != null ? request.getCollaborative() : false)
                .owner(owner)
                .build();

        if (request.getTrackIds() != null && !request.getTrackIds().isEmpty()) {
            Set<Track> tracks = request.getTrackIds().stream()
                    .map(trackId -> trackRepository.findById(trackId)
                            .orElseThrow(() -> new RuntimeException("Track not found with id: " + trackId)))
                    .collect(Collectors.toSet());
            playlist.setTracks(tracks);
        }

        playlist = playlistRepository.save(playlist);
        return mapToResponse(playlist);
    }

    @Transactional(readOnly = true)
    public PlaylistResponse getPlaylist(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        return mapToResponse(playlist);
    }

    @Transactional(readOnly = true)
    public List<PlaylistResponse> getAllPlaylists() {
        return playlistRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlaylistResponse> searchPlaylists(String name) {
        return playlistRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlaylistResponse> getPlaylistsByOwner(Long ownerId) {
        return playlistRepository.findByOwnerId(ownerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlaylistResponse> getFollowedPlaylists(Long userId) {
        return playlistRepository.findFollowedPlaylists(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlaylistResponse> getPublicPlaylists() {
        return playlistRepository.findPublicPlaylists().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlaylistResponse> getCollaborativePlaylists() {
        return playlistRepository.findCollaborativePlaylists().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addTrackToPlaylist(Long playlistId, Long trackId, Long userId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        
        if (!playlist.getOwner().getId().equals(userId) && !playlist.getCollaborative()) {
            throw new RuntimeException("You don't have permission to modify this playlist");
        }

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Track not found"));

        playlist.getTracks().add(track);
        playlistRepository.save(playlist);
    }

    @Transactional
    public void removeTrackFromPlaylist(Long playlistId, Long trackId, Long userId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        
        if (!playlist.getOwner().getId().equals(userId) && !playlist.getCollaborative()) {
            throw new RuntimeException("You don't have permission to modify this playlist");
        }

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Track not found"));

        playlist.getTracks().remove(track);
        playlistRepository.save(playlist);
    }

    @Transactional
    public void followPlaylist(Long playlistId, Long userId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        playlist.getFollowers().add(user);
        playlistRepository.save(playlist);
    }

    @Transactional
    public void unfollowPlaylist(Long playlistId, Long userId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        playlist.getFollowers().remove(user);
        playlistRepository.save(playlist);
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