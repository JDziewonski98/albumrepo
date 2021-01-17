package com.imagerepo.albumrepo.repositories;

import com.imagerepo.albumrepo.enums.Genre;
import com.imagerepo.albumrepo.models.AlbumModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.HashSet;
import java.util.Optional;

public interface AlbumRepo extends CrudRepository<AlbumModel, Long> {
    Optional<AlbumModel> findByTitle(String title);

    @Query("FROM AlbumModel WHERE lower(title) LIKE lower(concat('%',?1,'%')) OR lower(description) LIKE " +
            "lower(concat('%',?1,'%')) OR lower(artist) LIKE lower(concat('%',?1,'%'))")
    HashSet<AlbumModel> findByMatchingString(String query);

    @Query("SELECT a FROM AlbumModel a JOIN a.genres g WHERE g = ?1")
    HashSet<AlbumModel> findByMatchingAnyGenre(Genre genre);

    @Query("SELECT distinct a FROM AlbumModel a JOIN a.genres g WHERE g in ?1 group by a.id having count(a.id) = ?2")
    HashSet<AlbumModel> findByExactGenre(Genre[] genres, long genrelistlength);
}
