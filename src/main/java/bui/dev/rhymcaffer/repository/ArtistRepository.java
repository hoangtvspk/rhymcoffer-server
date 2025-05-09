package bui.dev.rhymcaffer.repository;

import bui.dev.rhymcaffer.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findByNameContainingIgnoreCase(String name);

    @Query("SELECT a FROM Artist a WHERE a.popularity >= :minPopularity ORDER BY a.popularity DESC")
    List<Artist> findPopularArtists(@Param("minPopularity") int minPopularity);

    @Query("SELECT a FROM Artist a JOIN a.followers f WHERE f.id = :userId")
    List<Artist> findFollowedArtists(@Param("userId") Long userId);

    @Query("SELECT a FROM Artist a " +
            "LEFT JOIN FETCH a.albums " +
            "LEFT JOIN FETCH a.tracks " +
            "WHERE a.id = :id")
    Artist findArtistById(@Param("id") Long id);
}