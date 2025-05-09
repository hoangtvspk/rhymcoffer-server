package bui.dev.rhymcaffer.repository;

import bui.dev.rhymcaffer.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByNameContainingIgnoreCase(String name);

    @Query("SELECT a FROM Album a JOIN a.artists ar WHERE ar.id = :artistId")
    List<Album> findByArtistId(@Param("artistId") Long artistId);

    @Query("SELECT a FROM Album a JOIN a.followers f WHERE f.id = :userId")
    List<Album> findSavedAlbums(@Param("userId") Long userId);

    @Query("SELECT a FROM Album a WHERE a.releaseDate >= :date ORDER BY a.releaseDate DESC")
    List<Album> findNewReleases(@Param("date") String date);

    List<Album> findByArtists_Id(Long artistId);

    List<Album> findByReleaseDateAfter(String date);

    @Query("SELECT a FROM Album a " +
            "LEFT JOIN FETCH a.artists " +
            "LEFT JOIN FETCH a.tracks " +
            "LEFT JOIN FETCH a.followers " +
            "WHERE a.id = :id")
    Album findAlbumById(@Param("id") Long id);

}