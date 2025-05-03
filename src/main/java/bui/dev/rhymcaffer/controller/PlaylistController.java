package bui.dev.rhymcaffer.controller;

import bui.dev.rhymcaffer.dto.request.PlaylistRequest;
import bui.dev.rhymcaffer.dto.response.PlaylistResponse;
import bui.dev.rhymcaffer.security.UserDetailsImpl;
import bui.dev.rhymcaffer.service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @PostMapping
    public ResponseEntity<PlaylistResponse> createPlaylist(
            @Valid @RequestBody PlaylistRequest request,
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(playlistService.createPlaylist(request, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponse> getPlaylist(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(playlistService.getPlaylist(id));
    }

    @GetMapping
    public ResponseEntity<List<PlaylistResponse>> getAllPlaylists() {
        return ResponseEntity.ok(playlistService.getAllPlaylists());
    }

    @GetMapping("/search")
    public ResponseEntity<List<PlaylistResponse>> searchPlaylists(
            @RequestParam String name
    ) {
        return ResponseEntity.ok(playlistService.searchPlaylists(name));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<PlaylistResponse>> getPlaylistsByOwner(
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(playlistService.getPlaylistsByOwner(userId));
    }

    @GetMapping("/followed")
    public ResponseEntity<List<PlaylistResponse>> getFollowedPlaylists(
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(playlistService.getFollowedPlaylists(userId));
    }

    @GetMapping("/public")
    public ResponseEntity<List<PlaylistResponse>> getPublicPlaylists() {
        return ResponseEntity.ok(playlistService.getPublicPlaylists());
    }

    @GetMapping("/collaborative")
    public ResponseEntity<List<PlaylistResponse>> getCollaborativePlaylists() {
        return ResponseEntity.ok(playlistService.getCollaborativePlaylists());
    }

    @PostMapping("/{playlistId}/tracks/{trackId}")
    public ResponseEntity<Void> addTrackToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long trackId,
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        playlistService.addTrackToPlaylist(playlistId, trackId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    public ResponseEntity<Void> removeTrackFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long trackId,
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        playlistService.removeTrackFromPlaylist(playlistId, trackId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{playlistId}/follow")
    public ResponseEntity<Void> followPlaylist(
            @PathVariable Long playlistId,
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        playlistService.followPlaylist(playlistId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{playlistId}/unfollow")
    public ResponseEntity<Void> unfollowPlaylist(
            @PathVariable Long playlistId,
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        playlistService.unfollowPlaylist(playlistId, userId);
        return ResponseEntity.ok().build();
    }
} 