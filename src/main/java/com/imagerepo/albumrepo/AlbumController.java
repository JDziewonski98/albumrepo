package com.imagerepo.albumrepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@RestController
@CrossOrigin(origins = "http://localhost:8090")
@RequestMapping(path = "api/v1")
public class AlbumController {

    private final AlbumService albumService;

    private static final Logger LOG = LoggerFactory.getLogger(AlbumController.class);

    @Autowired
    public AlbumController(AlbumService albumRepo) {
        this.albumService = albumRepo;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@ModelAttribute UploadAlbumWrapper model, BindingResult errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(model.getTitle()) || StringUtils.isEmpty(model.getArtist()) || model.getImgfile() == null) {
            LOG.info("Bad request!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            LOG.info("Uploading album with title {}", model.getArtist());
            this.albumService.uploadAlbum(model.getImgfile(), model.getTitle(), model.getDescription(), model.getGenres(), model.getArtist());
        }
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOG.info("Done.");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/getall")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(albumService.getAll(), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchForAlbums(@RequestParam String text, @RequestParam Genre[] genres, @RequestParam boolean exactMatch) {
        LOG.info("Search request on text {} with genres {} exact match {}", text, Arrays.toString(genres), exactMatch);
        HashSet<AlbumModel> textMatchedAlbums = new HashSet<>(Collections.emptySet());
        HashSet<AlbumModel> genreMatchedAlbums = new HashSet<>(Collections.emptySet());
        if (StringUtils.isEmpty(text) && genres.length == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!StringUtils.isEmpty(text)) {
            textMatchedAlbums = albumService.searchByKeyword(text);
            LOG.info("Got text matched albums.");
        }
        if (genres.length > 0) {
            if (exactMatch) {
                //search for albums that have EVERY one of the genre tags provided
                genreMatchedAlbums = albumService.searchByGenreMatchAll(genres);
                LOG.info("Got exact genre matched albums.");
            }
            else {
                //search for albums that have ANY OF the genre tags provided
                genreMatchedAlbums = albumService.searchByGenre(genres);
                LOG.info("Got any genre matched albums.");
            }
        }
        if (textMatchedAlbums.isEmpty()) {
            LOG.info("Returning just genrematched");
            return new ResponseEntity<>(genreMatchedAlbums, HttpStatus.CREATED);
        }
        else if (genreMatchedAlbums.isEmpty()) {
            LOG.info("Returning just textmatched");
            return new ResponseEntity<>(textMatchedAlbums, HttpStatus.CREATED);
        }
        else {
            //return intersection of the 2 search results
            LOG.info("genre matched: {}", genreMatchedAlbums.toString());
            LOG.info("text matched {}", textMatchedAlbums.toString());
            genreMatchedAlbums.retainAll(textMatchedAlbums);
            LOG.info("Combined: {}", genreMatchedAlbums.toString());
            return new ResponseEntity<>(genreMatchedAlbums, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAlbum(@RequestParam long deleteId) {
        try {
            albumService.deleteAlbum(deleteId);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
