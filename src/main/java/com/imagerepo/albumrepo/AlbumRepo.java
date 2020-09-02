package com.imagerepo.albumrepo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AlbumRepo extends CrudRepository<AlbumModel, Long> {
    Optional<AlbumModel> findByTitle(String title);

    @Query("FROM AlbumModel WHERE lower(title) LIKE lower(concat('%',?1,'%')) OR lower(description) LIKE " +
            "lower(concat('%',?1,'%')) OR lower(artist) LIKE lower(concat('%',?1,'%'))")
    ArrayList<AlbumModel> findByMatchingString(String query);
}
