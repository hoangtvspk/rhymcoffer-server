package bui.dev.rhymcaffer.controller;

import bui.dev.rhymcaffer.dto.album.AlbumResponse;
import bui.dev.rhymcaffer.dto.artist.ArtistListResponse;
import bui.dev.rhymcaffer.dto.artist.ArtistRequest;
import bui.dev.rhymcaffer.dto.artist.ArtistResponse;
import bui.dev.rhymcaffer.dto.common.BaseResponse;
import bui.dev.rhymcaffer.dto.track.TrackResponse;
import bui.dev.rhymcaffer.security.UserDetailsImpl;
import bui.dev.rhymcaffer.service.ArtistService;
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
                        @PathVariable Long id) {
                return ResponseEntity.ok(artistService.getArtist(id));
        }

        @GetMapping
        public ResponseEntity<BaseResponse<List<ArtistListResponse>>> getAllArtists() {
                return ResponseEntity.ok(artistService.getAllArtists());
        }

        @GetMapping("/search")
        public ResponseEntity<BaseResponse<List<ArtistListResponse>>> searchArtists(@RequestParam String name) {
                return ResponseEntity.ok(artistService.searchArtists(name));
        }

        @GetMapping("/popular")
        public ResponseEntity<BaseResponse<List<ArtistListResponse>>> getPopularArtists(
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

        @PostMapping("/{artistId}/tracks")
        public ResponseEntity<BaseResponse<Void>> addTracksToArtist(
                        @PathVariable Long artistId,
                        @RequestBody List<Long> trackIds) {
                return ResponseEntity.ok(artistService.addTracksToArtist(artistId, trackIds));
        }

        @DeleteMapping("/{artistId}/tracks")
        public ResponseEntity<BaseResponse<Void>> removeTracksFromArtist(
                        @PathVariable Long artistId,
                        @RequestBody List<Long> trackIds) {
                return ResponseEntity.ok(artistService.removeTracksFromArtist(artistId, trackIds));
        }

        @GetMapping("/{artistId}/albums")
        public ResponseEntity<BaseResponse<List<AlbumResponse>>> getAlbumsOfArtist(
                        @PathVariable Long artistId) {
                return ResponseEntity.ok(artistService.getAlbumsOfArtist(artistId));
        }

        @PostMapping("/{artistId}/albums")
        public ResponseEntity<BaseResponse<Void>> addAlbumsToArtist(
                        @PathVariable Long artistId,
                        @RequestBody List<Long> albumIds) {
                return ResponseEntity.ok(artistService.addAlbumsToArtist(artistId, albumIds));
        }

        @DeleteMapping("/{artistId}/albums")
        public ResponseEntity<BaseResponse<Void>> removeAlbumsFromArtist(
                        @PathVariable Long artistId,
                        @RequestBody List<Long> albumIds) {
                return ResponseEntity.ok(artistService.removeAlbumsFromArtist(artistId, albumIds));
        }
}