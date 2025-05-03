package bui.dev.rhymcaffer.repository;

import bui.dev.rhymcaffer.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT t FROM Track t JOIN t.artists a WHERE a.id = :artistId")
    List<Track> findByArtistId(@Param("artistId") Long artistId);
    
    @Query("SELECT t FROM Track t WHERE t.album.id = :albumId")
    List<Track> findByAlbumId(@Param("albumId") Long albumId);
    
    @Query("SELECT t FROM Track t JOIN t.savedByUsers u WHERE u.id = :userId")
    List<Track> findSavedTracks(@Param("userId") Long userId);
    
    @Query("SELECT t FROM Track t WHERE t.popularity >= :minPopularity ORDER BY t.popularity DESC")
    List<Track> findPopularTracks(@Param("minPopularity") int minPopularity);
} 