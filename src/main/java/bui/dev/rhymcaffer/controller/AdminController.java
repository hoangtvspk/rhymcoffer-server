package bui.dev.rhymcaffer.controller;

import bui.dev.rhymcaffer.dto.request.*;
import bui.dev.rhymcaffer.dto.response.*;
import bui.dev.rhymcaffer.security.UserDetailsImpl;
import bui.dev.rhymcaffer.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final UserService userService;
    private final ArtistService artistService;
    private final AlbumService albumService;
    private final TrackService trackService;
    private final PlaylistService playlistService;

    // User Management
    @GetMapping("/users")
    public ResponseEntity<BaseResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<BaseResponse<Void>> createUser(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<BaseResponse<Void>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    // Artist Management
    @GetMapping("/artists")
    public ResponseEntity<BaseResponse<List<ArtistResponse>>> getAllArtists() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    @PostMapping("/artists")
    public ResponseEntity<BaseResponse<Void>> createArtist(@Valid @RequestBody ArtistRequest request) {
        return ResponseEntity.ok(artistService.createArtist(request));
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<BaseResponse<ArtistResponse>> getArtist(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean expandAlbums,
            @RequestParam(defaultValue = "false") boolean expandTracks) {
        return ResponseEntity.ok(artistService.getArtist(id, expandAlbums, expandTracks));
    }

    @DeleteMapping("/artists/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteArtist(@PathVariable Long id) {
        return ResponseEntity.ok(artistService.deleteArtist(id));
    }

    // Album Management
    @GetMapping("/albums")
    public ResponseEntity<BaseResponse<List<AlbumResponse>>> getAllAlbums() {
        return ResponseEntity.ok(albumService.getAllAlbums());
    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<BaseResponse<AlbumResponse>> getAlbum(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.getAlbum(id));
    }

    @PostMapping("/albums")
    public ResponseEntity<BaseResponse<Void>> createAlbum(@Valid @RequestBody AlbumRequest request) {
        return ResponseEntity.ok(albumService.createAlbum(request));
    }

    @DeleteMapping("/albums/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteAlbum(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.deleteAlbum(id));
    }

    // Track Management
    @GetMapping("/tracks")
    public ResponseEntity<BaseResponse<List<TrackListResponse>>> getAllTracks() {
        return ResponseEntity.ok(trackService.getAllTracks());
    }

    @GetMapping("/tracks/{id}")
    public ResponseEntity<BaseResponse<TrackResponse>> getTrack(@PathVariable Long id) {
        return ResponseEntity.ok(trackService.getTrack(id));
    }

    @PostMapping("/tracks")
    public ResponseEntity<BaseResponse<Void>> createTrack(@Valid @RequestBody TrackRequest request) {
        return ResponseEntity.ok(trackService.createTrack(request));
    }

    @PutMapping("/tracks/{id}")
    public ResponseEntity<BaseResponse<Void>> updateTrack(
            @PathVariable Long id,
            @RequestBody TrackRequest request) {
        return ResponseEntity.ok(trackService.updateTrack(id, request));
    }

    @DeleteMapping("/tracks/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteTrack(@PathVariable Long id) {
        return ResponseEntity.ok(trackService.deleteTrack(id));
    }

    // Playlist Management
    @GetMapping("/playlists")
    public ResponseEntity<BaseResponse<List<PlaylistResponse>>> getAllPlaylists() {
        return ResponseEntity.ok(playlistService.getAllPlaylists());
    }

    @PostMapping("/playlists")
    public ResponseEntity<BaseResponse<Void>> createPlaylist(
            @Valid @RequestBody PlaylistRequest request,
            Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(playlistService.createPlaylist(request, userId));
    }

    @DeleteMapping("/playlists/{id}")
    public ResponseEntity<BaseResponse<Void>> deletePlaylist(@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.deletePlaylist(id));
    }

    // // Bulk Operations
    // @PostMapping("/bulk/artists")
    // public ResponseEntity<List<ArtistResponse>> createArtists(@Valid @RequestBody
    // List<ArtistRequest> requests) {
    // return ResponseEntity.ok(artistService.createArtists(requests));
    // }

    // @PostMapping("/bulk/albums")
    // public ResponseEntity<List<AlbumResponse>> createAlbums(@Valid @RequestBody
    // List<AlbumRequest> requests) {
    // return ResponseEntity.ok(albumService.createAlbums(requests));
    // }

    // @PostMapping("/bulk/tracks")
    // public ResponseEntity<List<TrackResponse>> createTracks(@Valid @RequestBody
    // List<TrackRequest> requests) {
    // return ResponseEntity.ok(trackService.createTracks(requests));
    // }
}