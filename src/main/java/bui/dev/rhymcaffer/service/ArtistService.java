package bui.dev.rhymcaffer.service;

import bui.dev.rhymcaffer.dto.request.ArtistRequest;
import bui.dev.rhymcaffer.dto.response.ArtistResponse;
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
    public ArtistResponse createArtist(ArtistRequest request) {
        Artist artist = Artist.builder()
                .name(request.getName())
                .imageUrl(request.getImageUrl())
                .description(request.getDescription())
                .popularity(request.getPopularity())
                .genres(request.getGenres())
                .build();

        artist = artistRepository.save(artist);
        return mapToResponse(artist);
    }

    @Transactional(readOnly = true)
    public ArtistResponse getArtist(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        return mapToResponse(artist);
    }

    @Transactional(readOnly = true)
    public List<ArtistResponse> getAllArtists() {
        return artistRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ArtistResponse> searchArtists(String name) {
        return artistRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ArtistResponse> getPopularArtists(int minPopularity) {
        return artistRepository.findPopularArtists(minPopularity).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void followArtist(Long artistId, Long userId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        artist.getFollowers().add(user);
        artistRepository.save(artist);
    }

    @Transactional
    public void unfollowArtist(Long artistId, Long userId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        artist.getFollowers().remove(user);
        artistRepository.save(artist);
    }

    private ArtistResponse mapToResponse(Artist artist) {
        return ArtistResponse.builder()
                .id(artist.getId())
                .name(artist.getName())
                .imageUrl(artist.getImageUrl())
                .description(artist.getDescription())
                .popularity(artist.getPopularity())
                .genres(artist.getGenres())
                .trackIds(artist.getTracks().stream()
                        .map(track -> track.getId())
                        .collect(Collectors.toSet()))
                .albumIds(artist.getAlbums().stream()
                        .map(album -> album.getId())
                        .collect(Collectors.toSet()))
                .followerIds(artist.getFollowers().stream()
                        .map(user -> user.getId())
                        .collect(Collectors.toSet()))
                .createdAt(artist.getCreatedAt())
                .updatedAt(artist.getUpdatedAt())
                .build();
    }
} 