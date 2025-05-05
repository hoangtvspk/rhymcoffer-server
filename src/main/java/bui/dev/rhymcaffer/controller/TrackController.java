package bui.dev.rhymcaffer.controller;

import bui.dev.rhymcaffer.dto.request.TrackRequest;
import bui.dev.rhymcaffer.dto.response.BaseResponse;
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
        public ResponseEntity<BaseResponse<Void>> createTrack(@RequestBody TrackRequest request) {
                return ResponseEntity.ok(trackService.createTrack(request));
        }

        @GetMapping("/{id}")
        public ResponseEntity<BaseResponse<TrackResponse>> getTrack(@PathVariable Long id) {
                return ResponseEntity.ok(trackService.getTrack(id));
        }

        @GetMapping
        public ResponseEntity<BaseResponse<List<TrackResponse>>> getAllTracks() {
                return ResponseEntity.ok(trackService.getAllTracks());
        }

        @GetMapping("/search")
        public ResponseEntity<BaseResponse<List<TrackResponse>>> searchTracks(@RequestParam String name) {
                return ResponseEntity.ok(trackService.searchTracks(name));
        }

        @GetMapping("/artist/{artistId}")
        public ResponseEntity<BaseResponse<List<TrackResponse>>> getTracksByArtist(@PathVariable Long artistId) {
                return ResponseEntity.ok(trackService.getTracksByArtist(artistId));
        }

        @GetMapping("/album/{albumId}")
        public ResponseEntity<BaseResponse<List<TrackResponse>>> getTracksByAlbum(@PathVariable Long albumId) {
                return ResponseEntity.ok(trackService.getTracksByAlbum(albumId));
        }

        @GetMapping("/saved")
        public ResponseEntity<BaseResponse<List<TrackResponse>>> getSavedTracks(Authentication authentication) {
                Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
                return ResponseEntity.ok(trackService.getSavedTracks(userId));
        }

        @GetMapping("/popular")
        public ResponseEntity<BaseResponse<List<TrackResponse>>> getPopularTracks(
                        @RequestParam(defaultValue = "70") int minPopularity) {
                return ResponseEntity.ok(trackService.getPopularTracks(minPopularity));
        }

        @PostMapping("/{trackId}/save")
        public ResponseEntity<BaseResponse<Void>> saveTrack(
                        @PathVariable Long trackId,
                        Authentication authentication) {
                Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
                return ResponseEntity.ok(trackService.saveTrack(trackId, userId));
        }

        @PostMapping("/{trackId}/unsave")
        public ResponseEntity<BaseResponse<Void>> unsaveTrack(
                        @PathVariable Long trackId,
                        Authentication authentication) {
                Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
                return ResponseEntity.ok(trackService.unsaveTrack(trackId, userId));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<BaseResponse<Void>> deleteTrack(@PathVariable Long id) {
                return ResponseEntity.ok(trackService.deleteTrack(id));
        }

        @PutMapping("/{id}")
        public ResponseEntity<BaseResponse<Void>> updateTrack(
                        @PathVariable Long id,
                        @RequestBody TrackRequest request) {
                return ResponseEntity.ok(trackService.updateTrack(id, request));
        }
}