package bui.dev.rhymcaffer.service;

import bui.dev.rhymcaffer.dto.album.TrackForAlbumResponse;
import bui.dev.rhymcaffer.dto.artist.*;
import bui.dev.rhymcaffer.dto.album.AlbumResponse;
import bui.dev.rhymcaffer.dto.common.BaseResponse;
import bui.dev.rhymcaffer.dto.track.TrackResponse;
import bui.dev.rhymcaffer.model.Album;
import bui.dev.rhymcaffer.model.Artist;
import bui.dev.rhymcaffer.model.Track;
import bui.dev.rhymcaffer.model.User;
import bui.dev.rhymcaffer.repository.AlbumRepository;
import bui.dev.rhymcaffer.repository.ArtistRepository;
import bui.dev.rhymcaffer.repository.TrackRepository;
import bui.dev.rhymcaffer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArtistService {

        private final ArtistRepository artistRepository;
        private final UserRepository userRepository;
        private final TrackRepository trackRepository;
        private final AlbumRepository albumRepository;

        @Transactional
        public BaseResponse<Void> createArtist(ArtistRequest request) {
                try {
                        Artist artist = Artist.builder()
                                        .name(request.getName())
                                        .imageUrl(request.getImageUrl())
                                        .description(request.getDescription())
                                        .popularity(request.getPopularity())
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
        public BaseResponse<ArtistResponse> getArtist(Long id) {
                try {
                        Artist artist = artistRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Artist not found"));

                        ArtistResponse response = mapToResponse(artist);
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
        public BaseResponse<List<ArtistListResponse>> getAllArtists() {
                try {
                        List<Artist> artists = artistRepository.findAll();
                        List<ArtistListResponse> responses = artists.stream()
                                        .map(this::mapToListResponse)
                                        .toList();
                        return BaseResponse.<List<ArtistListResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<ArtistListResponse>>builder()
                                        .statusCode(500)
                                        .isSuccess(false)
                                        .message("Failed to retrieve artists: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<ArtistListResponse>> searchArtists(String name) {
                try {
                        List<Artist> artists = artistRepository.findByNameContainingIgnoreCase(name);
                        List<ArtistListResponse> responses = artists.stream()
                                        .map(this::mapToListResponse)
                                        .toList();
                        return BaseResponse.<List<ArtistListResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<ArtistListResponse>>builder()
                                        .statusCode(400)
                                        .isSuccess(false)
                                        .message("Search failed: " + e.getMessage())
                                        .build();
                }
        }

        @Transactional(readOnly = true)
        public BaseResponse<List<ArtistListResponse>> getPopularArtists(int minPopularity) {
                try {
                        List<Artist> artists = artistRepository.findPopularArtists(minPopularity);
                        List<ArtistListResponse> responses = artists.stream()
                                        .map(this::mapToListResponse)
                                        .toList();
                        return BaseResponse.<List<ArtistListResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<ArtistListResponse>>builder()
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

        // get tracks of an artist
        @Transactional(readOnly = true)
        public BaseResponse<List<TrackResponse>> getTracksOfArtist(Long artistId) {
                try {
                        Artist artist = artistRepository.findArtistById(artistId);
                        if (artist == null) {
                                return BaseResponse.<List<TrackResponse>>builder()
                                                .statusCode(404)
                                                .isSuccess(false)
                                                .message("Artist not found")
                                                .build();
                        }
                        ArtistResponse response = mapToResponse(artist);
                        System.out.println("Artist: " + response);
                        Set<Track> tracks = artist.getTracks();
                        List<TrackResponse> responses = tracks.stream()
                                        .map(this::mapToTrackResponse)
                                        .toList();
                        return BaseResponse.<List<TrackResponse>>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Success")
                                        .data(responses)
                                        .build();
                } catch (Exception e) {
                        return BaseResponse.<List<TrackResponse>>builder()
                                        .statusCode(500)
                                        .isSuccess(false)
                                        .message("Internal server error")
                                        .build();
                }
        }

        private TrackResponse mapToTrackResponse(Track track) {
                return TrackResponse.builder()
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
                                .build();
        }

        @Transactional
        public BaseResponse<Void> addTracksToArtist(Long artistId, List<Long> trackIds) {
                try {
                        Artist artist = artistRepository.findById(artistId)
                                        .orElseThrow(() -> new RuntimeException("Artist not found"));
                        List<Track> tracks = trackRepository.findAllById(trackIds);

                        if (tracks.size() != trackIds.size()) {
                                return BaseResponse.<Void>builder()
                                                .statusCode(404)
                                                .isSuccess(false)
                                                .message("One or more tracks not found")
                                                .build();
                        }

                        for (Track track : tracks) {
                                track.getArtists().add(artist);
                                artist.getTracks().add(track);
                        }

                        artistRepository.save(artist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Tracks added to artist successfully")
                                        .build();
                } catch (RuntimeException e) {
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
        public BaseResponse<Void> removeTracksFromArtist(Long artistId, List<Long> trackIds) {
                try {
                        Artist artist = artistRepository.findById(artistId)
                                        .orElseThrow(() -> new RuntimeException("Artist not found"));
                        List<Track> tracks = trackRepository.findAllById(trackIds);

                        if (tracks.size() != trackIds.size()) {
                                return BaseResponse.<Void>builder()
                                                .statusCode(404)
                                                .isSuccess(false)
                                                .message("One or more tracks not found")
                                                .build();
                        }

                        for (Track track : tracks) {
                                track.getArtists().remove(artist);
                                artist.getTracks().remove(track);
                        }

                        artistRepository.save(artist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Tracks removed from artist successfully")
                                        .build();
                } catch (RuntimeException e) {
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

        // get all albums of an artist
        @Transactional(readOnly = true)
        public BaseResponse<List<AlbumResponse>> getAlbumsOfArtist(Long artistId) {
                try {
                        Artist artist = artistRepository.findArtistById(artistId);
                        if (artist == null) {
                                return BaseResponse.<List<AlbumResponse>>builder()
                                                .statusCode(404)
                                                .isSuccess(false)
                                                .message("Artist not found")
                                                .build();
                        }
                        Set<Album> albums = artist.getAlbums();
                        List<AlbumResponse> responses = albums.stream()
                                        .map(this::mapToAlbumResponse)
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
                                        .message("Internal server error")
                                        .build();
                }
        }

        private AlbumResponse mapToAlbumResponse(Album album) {
                return AlbumResponse.builder()
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
        }

        @Transactional
        public BaseResponse<Void> addAlbumsToArtist(Long artistId, List<Long> albumIds) {
                try {
                        Artist artist = artistRepository.findById(artistId)
                                        .orElseThrow(() -> new RuntimeException("Artist not found"));
                        List<Album> albums = albumRepository.findAllById(albumIds);

                        if (albums.size() != albumIds.size()) {
                                return BaseResponse.<Void>builder()
                                                .statusCode(404)
                                                .isSuccess(false)
                                                .message("One or more albums not found")
                                                .build();
                        }

                        for (Album album : albums) {
                                album.getArtists().add(artist);
                                artist.getAlbums().add(album);
                        }

                        artistRepository.save(artist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Albums added to artist successfully")
                                        .build();
                } catch (RuntimeException e) {
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
        public BaseResponse<Void> removeAlbumsFromArtist(Long artistId, List<Long> albumIds) {
                try {
                        Artist artist = artistRepository.findById(artistId)
                                        .orElseThrow(() -> new RuntimeException("Artist not found"));
                        List<Album> albums = albumRepository.findAllById(albumIds);

                        if (albums.size() != albumIds.size()) {
                                return BaseResponse.<Void>builder()
                                                .statusCode(404)
                                                .isSuccess(false)
                                                .message("One or more albums not found")
                                                .build();
                        }

                        for (Album album : albums) {
                                album.getArtists().remove(artist);
                                artist.getAlbums().remove(album);
                        }

                        artistRepository.save(artist);
                        return BaseResponse.<Void>builder()
                                        .statusCode(200)
                                        .isSuccess(true)
                                        .message("Albums removed from artist successfully")
                                        .build();
                } catch (RuntimeException e) {
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

        private ArtistResponse mapToResponse(Artist artist) {
                ArtistResponse.ArtistResponseBuilder builder = ArtistResponse.builder()
                                .id(artist.getId())
                                .name(artist.getName())
                                .imageUrl(artist.getImageUrl())
                                .description(artist.getDescription())
                                // .tracks(artist.getTracks() != null ? artist.getTracks().stream()
                                // .map(track -> TrackForArtistResponse.builder()
                                // .id(track.getId())
                                // .name(track.getName())
                                // .imageUrl(track.getImageUrl())
                                // .durationMs(track.getDurationMs())
                                // .popularity(track.getPopularity())
                                // .trackUrl(track.getTrackUrl())
                                // .trackNumber(track.getTrackNumber())
                                // .explicit(track.getExplicit())
                                // .isrc(track.getIsrc())
                                // .createdAt(track.getCreatedAt())
                                // .updatedAt(track.getUpdatedAt())
                                // .build())
                                // .collect(Collectors.toList()) : new ArrayList<>())
                                // .albums(artist.getAlbums() != null ? artist.getAlbums().stream()
                                // .map(album -> AlbumForArtistResponse.builder()
                                // .id(album.getId())
                                // .name(album.getName())
                                // .imageUrl(album.getImageUrl())
                                // .description(album.getDescription())
                                // .popularity(album.getPopularity())
                                // .releaseDate(album.getReleaseDate())
                                // .albumType(album.getAlbumType())
                                // .createdAt(album.getCreatedAt())
                                // .updatedAt(album.getUpdatedAt())
                                // .build())
                                // .collect(Collectors.toList()) : new ArrayList<>())
                                .popularity(artist.getPopularity())
                                .createdAt(artist.getCreatedAt())
                                .updatedAt(artist.getUpdatedAt());

                return builder.build();
        }

        private ArtistListResponse mapToListResponse(Artist artist) {
                ArtistListResponse.ArtistListResponseBuilder builder = ArtistListResponse.builder()
                                .id(artist.getId())
                                .name(artist.getName())
                                .imageUrl(artist.getImageUrl())
                                .description(artist.getDescription())
                                .popularity(artist.getPopularity())
                                .createdAt(artist.getCreatedAt())
                                .updatedAt(artist.getUpdatedAt());
                return builder.build();
        }
}