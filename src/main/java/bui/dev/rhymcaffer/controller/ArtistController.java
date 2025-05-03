package bui.dev.rhymcaffer.controller;

import bui.dev.rhymcaffer.dto.request.ArtistRequest;
import bui.dev.rhymcaffer.dto.response.ArtistResponse;
import bui.dev.rhymcaffer.security.UserDetailsImpl;
import bui.dev.rhymcaffer.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping
    public ResponseEntity<ArtistResponse> createArtist(
            @Valid @RequestBody ArtistRequest request
    ) {
        return ResponseEntity.ok(artistService.createArtist(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponse> getArtist(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(artistService.getArtist(id));
    }

    @GetMapping
    public ResponseEntity<List<ArtistResponse>> getAllArtists() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArtistResponse>> searchArtists(
            @RequestParam String name
    ) {
        return ResponseEntity.ok(artistService.searchArtists(name));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ArtistResponse>> getPopularArtists(
            @RequestParam(defaultValue = "50") int minPopularity
    ) {
        return ResponseEntity.ok(artistService.getPopularArtists(minPopularity));
    }

    @PostMapping("/{artistId}/follow")
    public ResponseEntity<Void> followArtist(
            @PathVariable Long artistId,
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        artistService.followArtist(artistId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{artistId}/unfollow")
    public ResponseEntity<Void> unfollowArtist(
            @PathVariable Long artistId,
            Authentication authentication
    ) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        artistService.unfollowArtist(artistId, userId);
        return ResponseEntity.ok().build();
    }
} 