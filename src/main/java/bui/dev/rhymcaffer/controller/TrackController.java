package bui.dev.rhymcaffer.controller;

import bui.dev.rhymcaffer.dto.request.TrackRequest;
import bui.dev.rhymcaffer.dto.response.TrackResponse;
import bui.dev.rhymcaffer.security.UserDetailsImpl;
import bui.dev.rhymcaffer.service.TrackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;

    @PostMapping
    public ResponseEntity<TrackResponse> createTrack(
            @Valid @RequestBody TrackRequest request
    ) {
        return ResponseEntity.ok(trackService.createTrack(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackResponse> getTrack(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(trackService.getTrack(id));
    }

    @GetMapping
    public ResponseEntity<List<TrackResponse>> getAllTracks() {
        return ResponseEntity.ok(trackService.getAllTracks());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrackResponse>> searchTracks(
            @RequestParam String name
    ) {
        return ResponseEntity.ok(trackService.searchTracks(name));
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<TrackResponse>> getTracksByArtist(
            @PathVariable Long artistId
    ) {
        return ResponseEntity.ok(trackService.getTracksByArtist(artistId));
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<TrackResponse>> getTracksByAlbum(
            @PathVariable Long albumId
    ) {
        return ResponseEntity.ok(trackService.getTracksByAlbum(albumId));
    }

    @GetMapping("/saved")
    public ResponseEntity<List<TrackResponse>> getSavedTracks(
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(trackService.getSavedTracks(userId));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<TrackResponse>> getPopularTracks(
            @RequestParam(defaultValue = "50") int minPopularity
    ) {
        return ResponseEntity.ok(trackService.getPopularTracks(minPopularity));
    }

    @PostMapping("/{trackId}/save")
    public ResponseEntity<Void> saveTrack(
            @PathVariable Long trackId,
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        trackService.saveTrack(trackId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{trackId}/unsave")
    public ResponseEntity<Void> unsaveTrack(
            @PathVariable Long trackId,
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        trackService.unsaveTrack(trackId, userId);
        return ResponseEntity.ok().build();
    }
} 