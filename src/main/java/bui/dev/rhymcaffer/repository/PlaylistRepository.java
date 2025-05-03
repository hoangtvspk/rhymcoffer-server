package bui.dev.rhymcaffer.repository;

import bui.dev.rhymcaffer.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT p FROM Playlist p WHERE p.owner.id = :userId")
    List<Playlist> findByOwnerId(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Playlist p JOIN p.followers f WHERE f.id = :userId")
    List<Playlist> findFollowedPlaylists(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Playlist p WHERE p.isPublic = true ORDER BY p.createdAt DESC")
    List<Playlist> findPublicPlaylists();
    
    @Query("SELECT p FROM Playlist p WHERE p.collaborative = true")
    List<Playlist> findCollaborativePlaylists();
} 