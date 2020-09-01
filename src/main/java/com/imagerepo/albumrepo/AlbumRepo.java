package com.imagerepo.albumrepo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumRepo extends JpaRepository<AlbumModel, Long> {
    Optional<AlbumModel> findByTitle(String title);
}
