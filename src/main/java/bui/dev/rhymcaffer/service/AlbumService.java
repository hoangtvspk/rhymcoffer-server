package bui.dev.rhymcaffer.service;

import bui.dev.rhymcaffer.dto.album.*;
import bui.dev.rhymcaffer.dto.common.BaseResponse;
import bui.dev.rhymcaffer.dto.track.TrackResponse;
import bui.dev.rhymcaffer.model.Album;
import bui.dev.rhymcaffer.model.Artist;
import bui.dev.rhymcaffer.model.Track;
import bui.dev.rhymcaffer.model.User;
import bui.dev.rhymcaffer.repository.AlbumRepository;
import bui.dev.rhymcaffer.repository.ArtistRepository;
import bui.dev.rhymcaffer.repository.UserRepository;
import bui.dev.rhymcaffer.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumService {

        private final AlbumRepository albumRepository;
        private final ArtistRepository artistRepository;
        private final UserRepository userRepository;
        private final TrackRepository trackRepository;

        @Transactional
        public BaseResponse<Void> createAlbum(AlbumRequest request) {
                try {
                        Album album = Album.builder()
                                        .name(request.getName())
                                        .imageUrl(request.getImageUrl())
                                        .description(request.getDescription())
                                        .popularity(request.getPopularity())
                                        .releaseDate(request.getReleaseDate())
                                        .albumType(request.getAlbumType())
                                        .build();

                        if (request.getArtistIds() != null && !request.getArtistIds().isEmpty()) {
                                Set<Artist> artists = request.getArtistIds().stream()
                                                .map(artistId -> artistRepository.findById(artistId)
                                                                .orElseThrow(() -> new RuntimeException(
                                                                                "Artist not found with id: "
                                                                                                + artistId)))
                                                .collect(Collectors.toSet());
                                album.setArtists(artists);
                        }

                        albumRepository.save(album);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Album created successfully")
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
        public BaseResponse<AlbumResponse> getAlbum(Long id) {
                try {
                        Album album = albumRepository.findAlbumById(id);
                        if (album == null) {
                                return BaseResponse.<AlbumResponse>builder()
                                                .statusCode(404)
                                                .isSuccess(false)
                                                .message("Album not found")
                                                .build();
                        }
                        AlbumResponse response = mapToResponse(album, true, true, true);
                        return BaseResponse.<AlbumResponse>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(response)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<AlbumResponse>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<AlbumResponse>> getAllAlbums() {
                try {
                        List<Album> albums = albumRepository.findAll();
                        List<AlbumResponse> responses = albums.stream()
                                        .map(album -> mapToResponse(album, false, false, false))
                                        .toList();
                        return BaseResponse.<List<AlbumResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<AlbumResponse>>builder()
                                        .statusCode(500)
                                        .isSuccess(false)
                                        .message("Failed to retrieve albums: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<AlbumResponse>> searchAlbums(String name) {
                try {
                        List<Album> albums = albumRepository.findByNameContainingIgnoreCase(name);
                        List<AlbumResponse> responses = albums.stream()
                                        .map(album -> mapToResponse(album, false, false, false))
                                        .toList();
                        return BaseResponse.<List<AlbumResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<AlbumResponse>>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message("Search failed: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<AlbumResponse>> getAlbumsByArtist(Long artistId) {
                try {
                        List<Album> albums = albumRepository.findByArtists_Id(artistId);
                        List<AlbumResponse> responses = albums.stream()
                                        .map(album -> mapToResponse(album, false, false, false))
                                        .toList();
                        return BaseResponse.<List<AlbumResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<List<AlbumResponse>>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<AlbumResponse>> getNewReleases(String date) {
                try {
                        List<Album> albums = albumRepository.findByReleaseDateAfter(date);
                        List<AlbumResponse> responses = albums.stream()
                                        .map(album -> mapToResponse(album, false, false, false))
                                        .toList();
                        return BaseResponse.<List<AlbumResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<AlbumResponse>>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message("Failed to get new releases: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional
        public BaseResponse<Void> saveAlbum(Long albumId, Long userId) {
                try {
                        Album album = albumRepository.findById(albumId)
                                        .orElseThrow(() -> new RuntimeException("Album not found"));
                        User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        album.getFollowers().add(user);
                        albumRepository.save(album);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Album saved successfully")
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
        public BaseResponse<Void> unsaveAlbum(Long albumId, Long userId) {
                try {
                        Album album = albumRepository.findById(albumId)
                                        .orElseThrow(() -> new RuntimeException("Album not found"));
                        User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        album.getFollowers().remove(user);
                        albumRepository.save(album);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Album unsaved successfully")
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
        public BaseResponse<Void> deleteAlbum(Long id) {
                try {
                        albumRepository.deleteById(id);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Album deleted successfully")
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
        public BaseResponse<Void> updateAlbum(Long id, AlbumRequest request) {
                try {
                        Album album = albumRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Album not found"));

                        if (request.getName() != null) {
                                album.setName(request.getName());
                        }
                        if (request.getImageUrl() != null) {
                                album.setImageUrl(request.getImageUrl());
                        }
                        if (request.getDescription() != null) {
                                album.setDescription(request.getDescription());
                        }
                        if (request.getPopularity() != null) {
                                album.setPopularity(request.getPopularity());
                        }
                        if (request.getReleaseDate() != null) {
                                album.setReleaseDate(request.getReleaseDate());
                        }
                        if (request.getAlbumType() != null) {
                                album.setAlbumType(request.getAlbumType());
                        }
                        if (request.getArtistIds() != null) {
                                Set<Artist> artists = request.getArtistIds().stream()
                                                .map(artistId -> artistRepository.findById(artistId)
                                                                .orElseThrow(() -> new RuntimeException(
                                                                                "Artist not found with id: "
                                                                                                + artistId)))
                                                .collect(Collectors.toSet());
                                album.setArtists(artists);
                        }
                        albumRepository.save(album);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Album updated successfully")
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
        public BaseResponse<Void> addTracksToAlbum(Long albumId, List<Long> trackIds) {
                try {
                        Album album = albumRepository.findById(albumId)
                                        .orElseThrow(() -> new RuntimeException("Album not found"));
                        List<Track> tracks = trackRepository.findAllById(trackIds);

                        album.getTracks().addAll(tracks);
                        albumRepository.save(album);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Tracks added to album successfully")
                                        .build();
                } catch (NoSuchElementException e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(500)
                                        .isSuccess(false)
                                        .message("Internal server error")
                                        .build();
                }
        }

        @Transactional
        public BaseResponse<Void> removeTracksFromAlbum(Long albumId, List<Long> trackIds) {
                try {
                        Album album = albumRepository.findById(albumId)
                                        .orElseThrow(() -> new RuntimeException("Album not found"));
                        List<Track> tracks = trackRepository.findAllById(trackIds);

                        album.getTracks().removeAll(tracks);
                        albumRepository.save(album);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Track removed from album successfully")
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<TrackForAlbumResponse>> getAlbumTracks(Long albumId) {
                try {
                        Album album = albumRepository.findById(albumId)
                                        .orElseThrow(() -> new RuntimeException("Album not found"));
                        List<TrackForAlbumResponse> tracks = album.getTracks().stream()
                                        .map(track -> TrackForAlbumResponse.builder()
                                                        .id(track.getId())
                                                        .name(track.getName())
                                                        .imageUrl(track.getImageUrl())
                                                        .durationMs(track.getDurationMs())
                                                        .popularity(track.getPopularity())
                                                        .trackUrl(track.getTrackUrl())
                                                        .trackNumber(track.getTrackNumber())
                                                        .explicit(track.getExplicit())
                                                        .isrc(track.getIsrc())

                                                        .createdAt(track.getCreatedAt())
                                                        .updatedAt(track.getUpdatedAt())
                                                        .build())
                                        .toList();
                        return BaseResponse.<List<TrackForAlbumResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(tracks)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<List<TrackForAlbumResponse>>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        private AlbumResponse mapToResponse(Album album, boolean includeArtists, boolean includeTracks,
                        boolean includeFollowers) {
                AlbumResponse response = AlbumResponse.builder()
                                .id(album.getId())
                                .name(album.getName())
                                .imageUrl(album.getImageUrl())
                                .description(album.getDescription())
                                .popularity(album.getPopularity())
                                .releaseDate(album.getReleaseDate())
                                .albumType(album.getAlbumType())
                                .createdAt(album.getCreatedAt())
                                .updatedAt(album.getUpdatedAt())
                                .build();

                if (includeArtists) {
                        List<ArtistForAlbumResponse> artistResponses = new ArrayList<>();
                        if (album.getArtists() != null) {
                                artistResponses = album.getArtists().stream()
                                                .map(artist -> ArtistForAlbumResponse.builder()
                                                                .id(artist.getId())
                                                                .name(artist.getName())
                                                                .imageUrl(artist.getImageUrl())
                                                                .description(artist.getDescription())
                                                                .popularity(artist.getPopularity())
                                                                .createdAt(artist.getCreatedAt())
                                                                .updatedAt(artist.getUpdatedAt())
                                                                .build())
                                                .collect(Collectors.toList());

                        }
                        response.setArtists(artistResponses);

                }

                if (includeTracks) {
                        List<TrackForAlbumResponse> trackResponses = new ArrayList<>();
                        if (album.getTracks() != null) {
                                trackResponses = album.getTracks().stream()
                                                .map(track -> TrackForAlbumResponse.builder()
                                                                .id(track.getId())
                                                                .name(track.getName())
                                                                .imageUrl(track.getImageUrl())
                                                                .durationMs(track.getDurationMs())
                                                                .popularity(track.getPopularity())
                                                                .trackUrl(track.getTrackUrl())
                                                                .trackNumber(track.getTrackNumber())
                                                                .explicit(track.getExplicit())
                                                                .isrc(track.getIsrc())
                                                                .createdAt(track.getCreatedAt())
                                                                .updatedAt(track.getUpdatedAt())
                                                                .build())
                                                .toList();
                        }
                        response.setTracks(trackResponses);
                }

                if (includeFollowers) {
                        List<UserForAlbumResponse> followers = new ArrayList<>();
                        if (album.getFollowers() != null) {
                                followers = album.getFollowers().stream().map(user -> UserForAlbumResponse.builder()
                                                .id(user.getId())
                                                .displayName(user.getDisplayName())
                                                .imageUrl(user.getImageUrl())
                                                .build()).collect(Collectors.toList());
                        }
                        response.setFollowers(followers);
                }

                return response;
        }
}