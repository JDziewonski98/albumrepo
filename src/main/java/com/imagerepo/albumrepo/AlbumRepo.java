package com.imagerepo.albumrepo;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AlbumRepo extends CrudRepository<AlbumModel, Long> {
    Optional<AlbumModel> findByTitle(String title);
}
