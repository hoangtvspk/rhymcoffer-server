package bui.dev.rhymcaffer.controller;

import bui.dev.rhymcaffer.dto.request.PlaylistRequest;
import bui.dev.rhymcaffer.dto.response.BaseResponse;
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
    public ResponseEntity<BaseResponse<Void>> createPlaylist(
            @RequestBody PlaylistRequest request,
            Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(playlistService.createPlaylist(request, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PlaylistResponse>> getPlaylist(@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.getPlaylist(id));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<PlaylistResponse>>> getAllPlaylists() {
        return ResponseEntity.ok(playlistService.getAllPlaylists());
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<PlaylistResponse>>> searchPlaylists(@RequestParam String name) {
        return ResponseEntity.ok(playlistService.searchPlaylists(name));
    }

    @GetMapping("/owner")
    public ResponseEntity<BaseResponse<List<PlaylistResponse>>> getPlaylistsByOwner(Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(playlistService.getPlaylistsByOwner(userId));
    }

    @GetMapping("/followed")
    public ResponseEntity<BaseResponse<List<PlaylistResponse>>> getFollowedPlaylists(Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(playlistService.getFollowedPlaylists(userId));
    }

    @GetMapping("/public")
    public ResponseEntity<BaseResponse<List<PlaylistResponse>>> getPublicPlaylists() {
        return ResponseEntity.ok(playlistService.getPublicPlaylists());
    }


    @PostMapping("/{playlistId}/tracks/{trackId}")
    public ResponseEntity<BaseResponse<Void>> addTrackToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long trackId) {
        return ResponseEntity.ok(playlistService.addTrackToPlaylist(playlistId, trackId));
    }

    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    public ResponseEntity<BaseResponse<Void>> removeTrackFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long trackId) {
        return ResponseEntity.ok(playlistService.removeTrackFromPlaylist(playlistId, trackId));
    }

    @PostMapping("/{playlistId}/follow")
    public ResponseEntity<BaseResponse<Void>> followPlaylist(
            @PathVariable Long playlistId,
            Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(playlistService.followPlaylist(playlistId, userId));
    }

    @PostMapping("/{playlistId}/unfollow")
    public ResponseEntity<BaseResponse<Void>> unfollowPlaylist(
            @PathVariable Long playlistId,
            Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(playlistService.unfollowPlaylist(playlistId, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deletePlaylist(@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.deletePlaylist(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> updatePlaylist(
            @PathVariable Long id,
            @RequestBody PlaylistRequest request,
            Authentication authentication) {
        Long ownerId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(playlistService.updatePlaylist(id, request, ownerId));
    }
}