package bui.dev.rhymcaffer.controller;

import bui.dev.rhymcaffer.dto.request.ArtistRequest;
import bui.dev.rhymcaffer.dto.response.ArtistResponse;
import bui.dev.rhymcaffer.dto.response.BaseResponse;
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
        public ResponseEntity<BaseResponse<Void>> createArtist(@RequestBody ArtistRequest request) {
                return ResponseEntity.ok(artistService.createArtist(request));
        }

        @GetMapping("/{id}")
        public ResponseEntity<BaseResponse<ArtistResponse>> getArtist(
                        @PathVariable Long id,
                        @RequestParam(defaultValue = "false") boolean expandAlbums,
                        @RequestParam(defaultValue = "false") boolean expandTracks) {
                return ResponseEntity.ok(artistService.getArtist(id, expandAlbums, expandTracks));
        }

        @GetMapping
        public ResponseEntity<BaseResponse<List<ArtistResponse>>> getAllArtists() {
                return ResponseEntity.ok(artistService.getAllArtists());
        }

        @GetMapping("/search")
        public ResponseEntity<BaseResponse<List<ArtistResponse>>> searchArtists(@RequestParam String name) {
                return ResponseEntity.ok(artistService.searchArtists(name));
        }

        @GetMapping("/popular")
        public ResponseEntity<BaseResponse<List<ArtistResponse>>> getPopularArtists(
                        @RequestParam(defaultValue = "0") int minPopularity) {
                return ResponseEntity.ok(artistService.getPopularArtists(minPopularity));
        }

        @PostMapping("/{artistId}/follow")
        public ResponseEntity<BaseResponse<Void>> followArtist(
                        @PathVariable Long artistId,
                        Authentication authentication) {
                Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
                return ResponseEntity.ok(artistService.followArtist(artistId, userId));
        }

        @PostMapping("/{artistId}/unfollow")
        public ResponseEntity<BaseResponse<Void>> unfollowArtist(
                        @PathVariable Long artistId,
                        Authentication authentication) {
                Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
                return ResponseEntity.ok(artistService.unfollowArtist(artistId, userId));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<BaseResponse<Void>> deleteArtist(@PathVariable Long id) {
                return ResponseEntity.ok(artistService.deleteArtist(id));
        }

        @PutMapping("/{id}")
        public ResponseEntity<BaseResponse<Void>> updateArtist(
                        @PathVariable Long id,
                        @RequestBody ArtistRequest request) {
                return ResponseEntity.ok(artistService.updateArtist(id, request));
        }
}