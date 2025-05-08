package bui.dev.rhymcaffer;

import bui.dev.rhymcaffer.model.Track;
import bui.dev.rhymcaffer.model.Artist;
import bui.dev.rhymcaffer.repository.TrackRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TrackRepositoryTest {
    @Autowired
    private TrackRepository trackRepository;

    @Test
    void testFindByIdWithArtists() {
        Long trackId = 7L; // Đảm bảo DB có track này và có artist liên kết
        Track track = trackRepository.findByIdWithArtists(trackId);
        System.out.println("Track: " + track);
        System.out.println("Artists: " + track.getArtists());
        for (Artist artist : track.getArtists()) {
            System.out.println("Artist: " + artist.getId() + " - " + artist.getName());
        }
        assertThat(track).isNotNull();
        assertThat(track.getArtists()).isNotEmpty();
    }
}