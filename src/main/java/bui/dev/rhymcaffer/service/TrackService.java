package bui.dev.rhymcaffer.service;

import bui.dev.rhymcaffer.dto.request.TrackRequest;
import bui.dev.rhymcaffer.dto.response.TrackResponse;
import bui.dev.rhymcaffer.model.*;
import bui.dev.rhymcaffer.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public TrackResponse createTrack(TrackRequest request) {
        Album album = null;
        if (request.getAlbumId() != null) {
            album = albumRepository.findById(request.getAlbumId())
                    .orElseThrow(() -> new RuntimeException("Album not found with id: " + request.getAlbumId()));
        }

        Track track = Track.builder()
                .name(request.getName())
                .imageUrl(request.getImageUrl())
                .durationMs(request.getDurationMs())
                .popularity(request.getPopularity())
                .previewUrl(request.getPreviewUrl())
                .trackNumber(request.getTrackNumber())
                .explicit(request.getExplicit())
                .isrc(request.getIsrc())
                .album(album)
                .build();

        if (request.getArtistIds() != null && !request.getArtistIds().isEmpty()) {
            Set<Artist> artists = request.getArtistIds().stream()
                    .map(artistId -> artistRepository.findById(artistId)
                            .orElseThrow(() -> new RuntimeException("Artist not found with id: " + artistId)))
                    .collect(Collectors.toSet());
            track.setArtists(artists);
        }

        track = trackRepository.save(track);
        return mapToResponse(track);
    }

    @Transactional(readOnly = true)
    public TrackResponse getTrack(Long id) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Track not found"));
        return mapToResponse(track);
    }

    @Transactional(readOnly = true)
    public List<TrackResponse> getAllTracks() {
        return trackRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TrackResponse> searchTracks(String name) {
        return trackRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TrackResponse> getTracksByArtist(Long artistId) {
        return trackRepository.findByArtistId(artistId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TrackResponse> getTracksByAlbum(Long albumId) {
        return trackRepository.findByAlbumId(albumId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TrackResponse> getSavedTracks(Long userId) {
        return trackRepository.findSavedTracks(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TrackResponse> getPopularTracks(int minPopularity) {
        return trackRepository.findPopularTracks(minPopularity).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveTrack(Long trackId, Long userId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Track not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        track.getSavedByUsers().add(user);
        trackRepository.save(track);
    }

    @Transactional
    public void unsaveTrack(Long trackId, Long userId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Track not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        track.getSavedByUsers().remove(user);
        trackRepository.save(track);
    }

    private TrackResponse mapToResponse(Track track) {
        return TrackResponse.builder()
                .id(track.getId())
                .name(track.getName())
                .imageUrl(track.getImageUrl())
                .durationMs(track.getDurationMs())
                .popularity(track.getPopularity())
                .previewUrl(track.getPreviewUrl())
                .trackNumber(track.getTrackNumber())
                .explicit(track.getExplicit())
                .isrc(track.getIsrc())
                .albumId(track.getAlbum() != null ? track.getAlbum().getId() : null)
                .artistIds(track.getArtists().stream()
                        .map(artist -> artist.getId())
                        .collect(Collectors.toSet()))
                .playlistIds(track.getPlaylists().stream()
                        .map(playlist -> playlist.getId())
                        .collect(Collectors.toSet()))
                .savedByUserIds(track.getSavedByUsers().stream()
                        .map(user -> user.getId())
                        .collect(Collectors.toSet()))
                .createdAt(track.getCreatedAt())
                .updatedAt(track.getUpdatedAt())
                .build();
    }
} 