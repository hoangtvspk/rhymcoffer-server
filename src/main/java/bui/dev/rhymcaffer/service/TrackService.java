package bui.dev.rhymcaffer.service;

import bui.dev.rhymcaffer.dto.track.*;
import bui.dev.rhymcaffer.dto.artist.ArtistResponse;
import bui.dev.rhymcaffer.dto.common.BaseResponse;
import bui.dev.rhymcaffer.model.*;
import bui.dev.rhymcaffer.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackService {

        private final TrackRepository trackRepository;
        private final AlbumRepository albumRepository;
        private final ArtistRepository artistRepository;
        private final UserRepository userRepository;
        private final PlaylistRepository playlistRepository;

        @Transactional
        public BaseResponse<Void> createTrack(TrackRequest request) {
                try {
                        Track track = Track.builder()
                                        .name(request.getName())
                                        .imageUrl(request.getImageUrl())
                                        .durationMs(request.getDurationMs())
                                        .popularity(request.getPopularity())
                                        .trackUrl(request.getTrackUrl())
                                        .trackNumber(request.getTrackNumber())
                                        .explicit(request.getExplicit())
                                        .isrc(request.getIsrc())
                                        .build();

                        if (request.getAlbumId() != null) {
                                Album album = albumRepository.findById(request.getAlbumId())
                                                .orElseThrow(() -> new RuntimeException("Album not found"));
                                track.setAlbum(album);
                        }

                        if (request.getArtistIds() != null && !request.getArtistIds().isEmpty()) {
                                Set<Artist> artists = request.getArtistIds().stream()
                                                .map(artistId -> artistRepository.findById(artistId)
                                                                .orElseThrow(() -> new RuntimeException(
                                                                                "Artist not found with id: "
                                                                                                + artistId)))
                                                .collect(Collectors.toSet());
                                track.setArtists(artists);
                        }

                        trackRepository.save(track);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Track created successfully")
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<TrackResponse> getTrack(Long id) {
                try {
                        Track track = trackRepository.findTrackById(id);

                        if (track == null) {
                                return BaseResponse.<TrackResponse>builder()
                                                .statusCode(404)
                                                .isSuccess(false)
                                                .message("Track not found")
                                                .build();
                        }
                        TrackResponse response = mapToResponse(track);
                        return BaseResponse.<TrackResponse>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(response)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<TrackResponse>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<TrackListResponse>> getAllTracks() {
                try {
                        List<Track> tracks = trackRepository.findAll();
                        List<TrackListResponse> responses = tracks.stream()
                                        .map(this::mapToListResponse)
                                        .toList();
                        return BaseResponse.<List<TrackListResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<TrackListResponse>>builder()
                                        .statusCode(500)
                                        .isSuccess(false)
                                        .message("Failed to retrieve tracks: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<TrackResponse>> searchTracks(String name) {
                try {
                        List<Track> tracks = trackRepository.findByNameContainingIgnoreCase(name);
                        List<TrackResponse> responses = tracks.stream()
                                        .map(this::mapToResponse)
                                        .toList();
                        return BaseResponse.<List<TrackResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<TrackResponse>>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message("Search failed: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<TrackResponse>> getTracksByArtist(Long artistId) {
                try {
                        List<Track> tracks = trackRepository.findByArtists_Id(artistId);
                        List<TrackResponse> responses = tracks.stream()
                                        .map(this::mapToResponse)
                                        .toList();
                        return BaseResponse.<List<TrackResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<List<TrackResponse>>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<TrackResponse>> getTracksByAlbum(Long albumId) {
                try {
                        List<Track> tracks = trackRepository.findByAlbum_Id(albumId);
                        List<TrackResponse> responses = tracks.stream()
                                        .map(this::mapToResponse)
                                        .toList();
                        return BaseResponse.<List<TrackResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<List<TrackResponse>>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<TrackResponse>> getSavedTracks(Long userId) {
                try {
                        List<Track> tracks = trackRepository.findBySavedByUsers_Id(userId);
                        List<TrackResponse> responses = tracks.stream()
                                        .map(this::mapToResponse)
                                        .toList();
                        return BaseResponse.<List<TrackResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<List<TrackResponse>>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<TrackResponse>> getPopularTracks(int minPopularity) {
                try {
                        List<Track> tracks = trackRepository.findByPopularityGreaterThanEqual(minPopularity);
                        List<TrackResponse> responses = tracks.stream()
                                        .map(this::mapToResponse)
                                        .toList();
                        return BaseResponse.<List<TrackResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<TrackResponse>>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message("Failed to get popular tracks: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional
        public BaseResponse<Void> saveTrack(Long trackId, Long userId) {
                try {
                        Track track = trackRepository.findById(trackId)
                                        .orElseThrow(() -> new RuntimeException("Track not found"));
                        User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        track.getSavedByUsers().add(user);
                        trackRepository.save(track);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Track saved successfully")
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional
        public BaseResponse<Void> unsaveTrack(Long trackId, Long userId) {
                try {
                        Track track = trackRepository.findById(trackId)
                                        .orElseThrow(() -> new RuntimeException("Track not found"));
                        User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        track.getSavedByUsers().remove(user);
                        trackRepository.save(track);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Track unsaved successfully")
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional
        public BaseResponse<Void> deleteTrack(Long id) {
                try {
                        trackRepository.deleteById(id);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Track deleted successfully")
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional
        public BaseResponse<Void> updateTrack(Long id, TrackRequest request) {
                try {
                        Track track = trackRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Track not found"));

                        if (request.getName() != null) {
                                track.setName(request.getName());
                        }
                        if (request.getImageUrl() != null) {
                                track.setImageUrl(request.getImageUrl());
                        }
                        if (request.getDurationMs() != null) {
                                track.setDurationMs(request.getDurationMs());
                        }
                        if (request.getPopularity() != null) {
                                track.setPopularity(request.getPopularity());
                        }
                        if (request.getTrackUrl() != null) {
                                track.setTrackUrl(request.getTrackUrl());
                        }
                        if (request.getTrackNumber() != null) {
                                track.setTrackNumber(request.getTrackNumber());
                        }
                        if (request.getExplicit() != null) {
                                track.setExplicit(request.getExplicit());
                        }
                        if (request.getIsrc() != null) {
                                track.setIsrc(request.getIsrc());
                        }
                        if (request.getAlbumId() != null) {
                                Album album = albumRepository.findById(request.getAlbumId())
                                                .orElseThrow(() -> new RuntimeException("Album not found"));
                                track.setAlbum(album);
                        }
                        if (request.getArtistIds() != null) {
                                Set<Artist> artists = request.getArtistIds().stream()
                                                .map(artistId -> artistRepository.findById(artistId)
                                                                .orElseThrow(() -> new RuntimeException(
                                                                                "Artist not found with id: "
                                                                                                + artistId)))
                                                .collect(Collectors.toSet());
                                track.setArtists(artists);
                        }
                        trackRepository.save(track);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Track updated successfully")
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        // add track to playlist
        @Transactional
        public BaseResponse<Void> addToPlaylist(Long trackId, Long playlistId) {
                try {
                        Track track = trackRepository.findById(trackId)
                                        .orElseThrow(() -> new RuntimeException("Track not found"));
                        Playlist playlist = playlistRepository.findById(playlistId)
                                        .orElseThrow(() -> new RuntimeException("Playlist not found"));

                        playlist.getTracks().add(track);
                        playlistRepository.save(playlist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Track added to playlist successfully")
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        private TrackListResponse mapToListResponse(Track track) {
                return TrackListResponse.builder()
                                .id(track.getId())
                                .name(track.getName())
                                .imageUrl(track.getImageUrl())
                                .durationMs(track.getDurationMs())
                                .popularity(track.getPopularity())
                                .trackUrl(track.getTrackUrl())
                                .trackNumber(track.getTrackNumber())
                                .explicit(track.getExplicit())
                                .isrc(track.getIsrc())
                                .albumId(track.getAlbum() != null ? track.getAlbum().getId() : null)
                                .createdAt(track.getCreatedAt())
                                .updatedAt(track.getUpdatedAt())
                                .build();
        }

        private TrackResponse mapToResponse(Track track) {
                TrackResponse trackResponse = TrackResponse.builder()
                                .id(track.getId())
                                .name(track.getName())
                                .imageUrl(track.getImageUrl())
                                .durationMs(track.getDurationMs())
                                .popularity(track.getPopularity())
                                .trackUrl(track.getTrackUrl())
                                .trackNumber(track.getTrackNumber())
                                .explicit(track.getExplicit())
                                .isrc(track.getIsrc())
                                .album(AlbumForTrackResponse.builder()
                                        .id(track.getAlbum().getId())
                                        .name(track.getAlbum().getName())
                                        .imageUrl(track.getAlbum().getImageUrl())
                                        .build())
                                .createdAt(track.getCreatedAt())
                                .updatedAt(track.getUpdatedAt())
                                .build();

                List<ArtistForTrackResponse> artists = new ArrayList<>();
                if (track.getArtists() != null) {
                        artists = track.getArtists().stream()
                                        .map(artist -> ArtistForTrackResponse.builder()
                                                        .id(artist.getId())
                                                        .name(artist.getName())
                                                        .imageUrl(artist.getImageUrl())
                                                        .build())
                                        .collect(Collectors.toList());
                }
                trackResponse.setArtists(artists);
                return trackResponse;
        }
}