//package bui.dev.rhymcaffer.controller;
//
//import bui.dev.rhymcaffer.dto.request.*;
//import bui.dev.rhymcaffer.dto.response.*;
//import bui.dev.rhymcaffer.security.UserDetailsImpl;
//import bui.dev.rhymcaffer.service.*;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin")
//@RequiredArgsConstructor
//@PreAuthorize("hasRole('ROLE_ADMIN')")
//public class AdminController {
//
//    private final UserService userService;
//    private final ArtistService artistService;
//    private final AlbumService albumService;
//    private final TrackService trackService;
//    private final PlaylistService playlistService;
//
//    // User Management
//    @GetMapping("/users")
//    public ResponseEntity<List<UserResponse>> getAllUsers() {
//        return ResponseEntity.ok(userService.getAllUsers());
//    }
//
//    @PostMapping("/users")
//    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
//        return ResponseEntity.ok(userService.createUser(request));
//    }
//
//    @PutMapping("/users/{id}")
//    public ResponseEntity<UserResponse> updateUser(
//            @PathVariable Long id,
//            @Valid @RequestBody UserRequest request) {
//        return ResponseEntity.ok(userService.updateUser(id, request));
//    }
//
//    @DeleteMapping("/users/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.ok().build();
//    }
//
//    // Artist Management
//    @GetMapping("/artists")
//    public ResponseEntity<List<ArtistResponse>> getAllArtists() {
//        return ResponseEntity.ok(artistService.getAllArtists());
//    }
//
//    @PostMapping("/artists")
//    public ResponseEntity<ArtistResponse> createArtist(@Valid @RequestBody ArtistRequest request) {
//        return ResponseEntity.ok(artistService.createArtist(request));
//    }
//
//    @PutMapping("/artists/{id}")
//    public ResponseEntity<ArtistResponse> updateArtist(
//            @PathVariable Long id,
//            @Valid @RequestBody ArtistRequest request) {
//        return ResponseEntity.ok(artistService.updateArtist(id, request));
//    }
//
//    @DeleteMapping("/artists/{id}")
//    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
//        artistService.deleteArtist(id);
//        return ResponseEntity.ok().build();
//    }
//
//    // Album Management
//    @GetMapping("/albums")
//    public ResponseEntity<List<AlbumResponse>> getAllAlbums() {
//        return ResponseEntity.ok(albumService.getAllAlbums());
//    }
//
//    @PostMapping("/albums")
//    public ResponseEntity<AlbumResponse> createAlbum(@Valid @RequestBody AlbumRequest request) {
//        return ResponseEntity.ok(albumService.createAlbum(request));
//    }
//
//    @PutMapping("/albums/{id}")
//    public ResponseEntity<AlbumResponse> updateAlbum(
//            @PathVariable Long id,
//            @Valid @RequestBody AlbumRequest request) {
//        return ResponseEntity.ok(albumService.updateAlbum(id, request));
//    }
//
//    @DeleteMapping("/albums/{id}")
//    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
//        albumService.deleteAlbum(id);
//        return ResponseEntity.ok().build();
//    }
//
//    // Track Management
//    @GetMapping("/tracks")
//    public ResponseEntity<List<TrackResponse>> getAllTracks() {
//        return ResponseEntity.ok(trackService.getAllTracks());
//    }
//
//    @PostMapping("/tracks")
//    public ResponseEntity<TrackResponse> createTrack(@Valid @RequestBody TrackRequest request) {
//        return ResponseEntity.ok(trackService.createTrack(request));
//    }
//
//    @PutMapping("/tracks/{id}")
//    public ResponseEntity<TrackResponse> updateTrack(
//            @PathVariable Long id,
//            @Valid @RequestBody TrackRequest request) {
//        return ResponseEntity.ok(trackService.updateTrack(id, request));
//    }
//
//    @DeleteMapping("/tracks/{id}")
//    public ResponseEntity<Void> deleteTrack(@PathVariable Long id) {
//        trackService.deleteTrack(id);
//        return ResponseEntity.ok().build();
//    }
//
//    // Playlist Management
//    @GetMapping("/playlists")
//    public ResponseEntity<List<PlaylistResponse>> getAllPlaylists() {
//        return ResponseEntity.ok(playlistService.getAllPlaylists());
//    }
//
//    @PostMapping("/playlists")
//    public ResponseEntity<PlaylistResponse> createPlaylist(
//            @Valid @RequestBody PlaylistRequest request,
//            Authentication authentication) {
//        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
//        return ResponseEntity.ok(playlistService.createPlaylist(request, userId));
//    }
//
//    @PutMapping("/playlists/{id}")
//    public ResponseEntity<PlaylistResponse> updatePlaylist(
//            @PathVariable Long id,
//            @Valid @RequestBody PlaylistRequest request) {
//        return ResponseEntity.ok(playlistService.updatePlaylist(id, request));
//    }
//
//    @DeleteMapping("/playlists/{id}")
//    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
//        playlistService.deletePlaylist(id);
//        return ResponseEntity.ok().build();
//    }
//
//    // Bulk Operations
//    @PostMapping("/bulk/artists")
//    public ResponseEntity<List<ArtistResponse>> createArtists(@Valid @RequestBody List<ArtistRequest> requests) {
//        return ResponseEntity.ok(artistService.createArtists(requests));
//    }
//
//    @PostMapping("/bulk/albums")
//    public ResponseEntity<List<AlbumResponse>> createAlbums(@Valid @RequestBody List<AlbumRequest> requests) {
//        return ResponseEntity.ok(albumService.createAlbums(requests));
//    }
//
//    @PostMapping("/bulk/tracks")
//    public ResponseEntity<List<TrackResponse>> createTracks(@Valid @RequestBody List<TrackRequest> requests) {
//        return ResponseEntity.ok(trackService.createTracks(requests));
//    }
//}