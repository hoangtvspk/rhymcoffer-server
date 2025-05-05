package bui.dev.rhymcaffer.service;

import bui.dev.rhymcaffer.dto.request.ArtistRequest;
import bui.dev.rhymcaffer.dto.response.AlbumResponse;
import bui.dev.rhymcaffer.dto.response.ArtistResponse;
import bui.dev.rhymcaffer.dto.response.BaseResponse;
import bui.dev.rhymcaffer.dto.response.TrackResponse;
import bui.dev.rhymcaffer.model.Artist;
import bui.dev.rhymcaffer.model.User;
import bui.dev.rhymcaffer.repository.ArtistRepository;
import bui.dev.rhymcaffer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistService {

        private final ArtistRepository artistRepository;
        private final UserRepository userRepository;

        @Transactional
        public BaseResponse<Void> createArtist(ArtistRequest request) {
                try {
                        Artist artist = Artist.builder()
                                        .name(request.getName())
                                        .imageUrl(request.getImageUrl())
                                        .description(request.getDescription())
                                        .popularity(request.getPopularity())
                                        .genres(request.getGenres())
                                        .build();

                        artistRepository.save(artist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Artist created successfully")
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
        public BaseResponse<ArtistResponse> getArtist(Long id, boolean expandAlbums, boolean expandTracks) {
                try {
                        Artist artist = artistRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Artist not found"));
                        ArtistResponse response = mapToResponse(artist, expandAlbums, expandTracks);
                        return BaseResponse.<ArtistResponse>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(response)
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<ArtistResponse>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<ArtistResponse>> getAllArtists() {
                try {
                        List<Artist> artists = artistRepository.findAll();
                        List<ArtistResponse> responses = artists.stream()
                                        .map(artist -> mapToResponse(artist, false, false))
                                        .toList();
                        return BaseResponse.<List<ArtistResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<ArtistResponse>>builder()
                                        .statusCode(500)
                                        .isSuccess(false)
                                        .message("Failed to retrieve artists: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<ArtistResponse>> searchArtists(String name) {
                try {
                        List<Artist> artists = artistRepository.findByNameContainingIgnoreCase(name);
                        List<ArtistResponse> responses = artists.stream()
                                        .map(artist -> mapToResponse(artist, false, false))
                                        .toList();
                        return BaseResponse.<List<ArtistResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<ArtistResponse>>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message("Search failed: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<ArtistResponse>> getPopularArtists(int minPopularity) {
                try {
                        List<Artist> artists = artistRepository.findPopularArtists(minPopularity);
                        List<ArtistResponse> responses = artists.stream()
                                        .map(artist -> mapToResponse(artist, false, false))
                                        .toList();
                        return BaseResponse.<List<ArtistResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<ArtistResponse>>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message("Failed to get popular artists: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional
        public BaseResponse<Void> followArtist(Long artistId, Long userId) {
                try {
                        Artist artist = artistRepository.findById(artistId)
                                        .orElseThrow(() -> new RuntimeException("Artist not found"));
                        User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        artist.getFollowers().add(user);
                        artistRepository.save(artist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Artist followed successfully")
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
        public BaseResponse<Void> unfollowArtist(Long artistId, Long userId) {
                try {
                        Artist artist = artistRepository.findById(artistId)
                                        .orElseThrow(() -> new RuntimeException("Artist not found"));
                        User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        artist.getFollowers().remove(user);
                        artistRepository.save(artist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Artist unfollowed successfully")
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
        public BaseResponse<Void> deleteArtist(Long id) {
                try {
                        artistRepository.deleteById(id);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Artist deleted successfully")
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
        public BaseResponse<Void> updateArtist(Long id, ArtistRequest request) {
                try {
                        Artist artist = artistRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Artist not found"));

                        if (request.getName() != null) {
                                artist.setName(request.getName());
                        }
                        if (request.getImageUrl() != null) {
                                artist.setImageUrl(request.getImageUrl());
                        }
                        if (request.getDescription() != null) {
                                artist.setDescription(request.getDescription());
                        }
                        if (request.getPopularity() != null) {
                                artist.setPopularity(request.getPopularity());
                        }
                        if (request.getGenres() != null) {
                                artist.setGenres(request.getGenres());
                        }
                        artistRepository.save(artist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Artist updated successfully")
                                        .build();
                } catch (RuntimeException e) {
                        return BaseResponse.<Void>builder()
                                        .statusCode(404)
                                        .isSuccess(false)
                                        .message(e.getMessage())
                                        .build();
                }
        }

        private ArtistResponse mapToResponse(Artist artist, boolean expandAlbums, boolean expandTracks) {
                ArtistResponse.ArtistResponseBuilder builder = ArtistResponse.builder()
                                .id(artist.getId())
                                .name(artist.getName())
                                .imageUrl(artist.getImageUrl())
                                .description(artist.getDescription())
                                .popularity(artist.getPopularity())
                                .genres(artist.getGenres())
                                .createdAt(artist.getCreatedAt())
                                .updatedAt(artist.getUpdatedAt());

                if (expandTracks) {
                        builder.tracks(artist.getTracks() == null ? List.of()
                                        : artist.getTracks().stream()
                                                        .map(track -> TrackResponse.builder()
                                                                        .id(track.getId())
                                                                        .name(track.getName())
                                                                        .imageUrl(track.getImageUrl())
                                                                        .durationMs(track.getDurationMs())
                                                                        .popularity(track.getPopularity())
                                                                        .previewUrl(track.getPreviewUrl())
                                                                        .trackNumber(track.getTrackNumber())
                                                                        .explicit(track.getExplicit())
                                                                        .isrc(track.getIsrc())
                                                                        .albumId(track.getAlbum() != null
                                                                                        ? track.getAlbum().getId()
                                                                                        : null)
                                                                        .artistIds(track.getArtists().stream()
                                                                                        .map(a -> a.getId())
                                                                                        .collect(Collectors.toSet()))
                                                                        .createdAt(track.getCreatedAt())
                                                                        .updatedAt(track.getUpdatedAt())
                                                                        .build())
                                                        .collect(Collectors.toList()));
                }
                if (expandAlbums) {
                        builder.albums(artist.getAlbums() == null ? List.of()
                                        : artist.getAlbums().stream()
                                                        .map(album -> AlbumResponse.builder()
                                                                        .id(album.getId())
                                                                        .name(album.getName())
                                                                        .imageUrl(album.getImageUrl())
                                                                        .description(album.getDescription())
                                                                        .popularity(album.getPopularity())
                                                                        .releaseDate(album.getReleaseDate())
                                                                        .albumType(album.getAlbumType())
                                                                        .artistIds(album.getArtists().stream()
                                                                                        .map(a -> a.getId())
                                                                                        .collect(Collectors.toSet()))
                                                                        .trackIds(album.getTracks().stream()
                                                                                        .map(t -> t.getId())
                                                                                        .collect(Collectors.toSet()))
                                                                        .followerIds(album.getFollowers().stream()
                                                                                        .map(u -> u.getId())
                                                                                        .collect(Collectors.toSet()))
                                                                        .createdAt(album.getCreatedAt())
                                                                        .updatedAt(album.getUpdatedAt())
                                                                        .build())
                                                        .collect(Collectors.toList()));
                }
                return builder.build();
        }
}