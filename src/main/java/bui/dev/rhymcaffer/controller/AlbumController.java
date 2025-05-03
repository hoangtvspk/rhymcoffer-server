package bui.dev.rhymcaffer.controller;

import bui.dev.rhymcaffer.dto.request.AlbumRequest;
import bui.dev.rhymcaffer.dto.response.AlbumResponse;
import bui.dev.rhymcaffer.security.UserDetailsImpl;
import bui.dev.rhymcaffer.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping
    public ResponseEntity<AlbumResponse> createAlbum(
            @Valid @RequestBody AlbumRequest request
    ) {
        return ResponseEntity.ok(albumService.createAlbum(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponse> getAlbum(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(albumService.getAlbum(id));
    }

    @GetMapping
    public ResponseEntity<List<AlbumResponse>> getAllAlbums() {
        return ResponseEntity.ok(albumService.getAllAlbums());
    }

    @GetMapping("/search")
    public ResponseEntity<List<AlbumResponse>> searchAlbums(
            @RequestParam String name
    ) {
        return ResponseEntity.ok(albumService.searchAlbums(name));
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<AlbumResponse>> getAlbumsByArtist(
            @PathVariable Long artistId
    ) {
        return ResponseEntity.ok(albumService.getAlbumsByArtist(artistId));
    }

    @GetMapping("/new-releases")
    public ResponseEntity<List<AlbumResponse>> getNewReleases(
            @RequestParam String date
    ) {
        return ResponseEntity.ok(albumService.getNewReleases(date));
    }

    @PostMapping("/{albumId}/save")
    public ResponseEntity<Void> saveAlbum(
            @PathVariable Long albumId,
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        albumService.saveAlbum(albumId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{albumId}/unsave")
    public ResponseEntity<Void> unsaveAlbum(
            @PathVariable Long albumId,
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        albumService.unsaveAlbum(albumId, userId);
        return ResponseEntity.ok().build();
    }
} 