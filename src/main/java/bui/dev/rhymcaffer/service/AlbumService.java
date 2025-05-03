package bui.dev.rhymcaffer.service;

import bui.dev.rhymcaffer.dto.request.AlbumRequest;
import bui.dev.rhymcaffer.dto.response.AlbumResponse;
import bui.dev.rhymcaffer.model.Album;
import bui.dev.rhymcaffer.model.Artist;
import bui.dev.rhymcaffer.model.User;
import bui.dev.rhymcaffer.repository.AlbumRepository;
import bui.dev.rhymcaffer.repository.ArtistRepository;
import bui.dev.rhymcaffer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;

    @Transactional
    public AlbumResponse createAlbum(AlbumRequest request) {
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
                            .orElseThrow(() -> new RuntimeException("Artist not found with id: " + artistId)))
                    .collect(Collectors.toSet());
            album.setArtists(artists);
        }

        album = albumRepository.save(album);
        return mapToResponse(album);
    }

    @Transactional(readOnly = true)
    public AlbumResponse getAlbum(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        return mapToResponse(album);
    }

    @Transactional(readOnly = true)
    public List<AlbumResponse> getAllAlbums() {
        return albumRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlbumResponse> searchAlbums(String name) {
        return albumRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlbumResponse> getAlbumsByArtist(Long artistId) {
        return albumRepository.findByArtistId(artistId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlbumResponse> getNewReleases(String date) {
        return albumRepository.findNewReleases(date).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveAlbum(Long albumId, Long userId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        album.getFollowers().add(user);
        albumRepository.save(album);
    }

    @Transactional
    public void unsaveAlbum(Long albumId, Long userId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        album.getFollowers().remove(user);
        albumRepository.save(album);
    }

    private AlbumResponse mapToResponse(Album album) {
        return AlbumResponse.builder()
                .id(album.getId())
                .name(album.getName())
                .imageUrl(album.getImageUrl())
                .description(album.getDescription())
                .popularity(album.getPopularity())
                .releaseDate(album.getReleaseDate())
                .albumType(album.getAlbumType())
                .artistIds(album.getArtists().stream()
                        .map(artist -> artist.getId())
                        .collect(Collectors.toSet()))
                .trackIds(album.getTracks().stream()
                        .map(track -> track.getId())
                        .collect(Collectors.toSet()))
                .followerIds(album.getFollowers().stream()
                        .map(user -> user.getId())
                        .collect(Collectors.toSet()))
                .createdAt(album.getCreatedAt())
                .updatedAt(album.getUpdatedAt())
                .build();
    }
} 