package bui.dev.rhymcaffer.controller;

import bui.dev.rhymcaffer.dto.request.AlbumRequest;
import bui.dev.rhymcaffer.dto.response.AlbumResponse;
import bui.dev.rhymcaffer.dto.response.BaseResponse;
import bui.dev.rhymcaffer.dto.response.TrackResponse;
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
        public ResponseEntity<BaseResponse<Void>> createAlbum(@RequestBody AlbumRequest request) {
                return ResponseEntity.ok(albumService.createAlbum(request));
        }

        @GetMapping("/{id}")
        public ResponseEntity<BaseResponse<AlbumResponse>> getAlbum(@PathVariable Long id) {
                return ResponseEntity.ok(albumService.getAlbum(id));
        }

        @GetMapping
        public ResponseEntity<BaseResponse<List<AlbumResponse>>> getAllAlbums() {
                return ResponseEntity.ok(albumService.getAllAlbums());
        }

        @GetMapping("/search")
        public ResponseEntity<BaseResponse<List<AlbumResponse>>> searchAlbums(@RequestParam String name) {
                return ResponseEntity.ok(albumService.searchAlbums(name));
        }

        @GetMapping("/artist/{artistId}")
        public ResponseEntity<BaseResponse<List<AlbumResponse>>> getAlbumsByArtist(@PathVariable Long artistId) {
                return ResponseEntity.ok(albumService.getAlbumsByArtist(artistId));
        }

        @GetMapping("/new-releases")
        public ResponseEntity<BaseResponse<List<AlbumResponse>>> getNewReleases(@RequestParam String date) {
                return ResponseEntity.ok(albumService.getNewReleases(date));
        }

        @PostMapping("/{albumId}/save")
        public ResponseEntity<BaseResponse<Void>> saveAlbum(
                        @PathVariable Long albumId,
                        Authentication authentication) {
                Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
                return ResponseEntity.ok(albumService.saveAlbum(albumId, userId));
        }

        @PostMapping("/{albumId}/unsave")
        public ResponseEntity<BaseResponse<Void>> unsaveAlbum(
                        @PathVariable Long albumId,
                        Authentication authentication) {
                Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
                return ResponseEntity.ok(albumService.unsaveAlbum(albumId, userId));
        }

        @PostMapping("/{albumId}/tracks/{trackId}")
        public ResponseEntity<BaseResponse<Void>> addTrackToAlbum(
                        @PathVariable Long albumId,
                        @PathVariable Long trackId) {
                return ResponseEntity.ok(albumService.addTrackToAlbum(albumId, trackId));
        }

        @DeleteMapping("/{albumId}/tracks/{trackId}")
        public ResponseEntity<BaseResponse<Void>> removeTrackFromAlbum(
                        @PathVariable Long albumId,
                        @PathVariable Long trackId) {
                return ResponseEntity.ok(albumService.removeTrackFromAlbum(albumId, trackId));
        }

        @GetMapping("/{albumId}/tracks")
        public ResponseEntity<BaseResponse<List<TrackResponse>>> getAlbumTracks(@PathVariable Long albumId) {
                return ResponseEntity.ok(albumService.getAlbumTracks(albumId));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<BaseResponse<Void>> deleteAlbum(@PathVariable Long id) {
                return ResponseEntity.ok(albumService.deleteAlbum(id));
        }

        @PutMapping("/{id}")
        public ResponseEntity<BaseResponse<Void>> updateAlbum(
                        @PathVariable Long id,
                        @RequestBody AlbumRequest request) {
                return ResponseEntity.ok(albumService.updateAlbum(id, request));
        }
}