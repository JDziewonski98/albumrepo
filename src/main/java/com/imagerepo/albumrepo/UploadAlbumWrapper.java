package com.imagerepo.albumrepo;

import org.springframework.web.multipart.MultipartFile;

public class UploadAlbumWrapper {
    private MultipartFile imgfile;
    private String title;
    private String description;
    private Genre[] genres;
    private String artist;

    public MultipartFile getImgfile() {
        return imgfile;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Genre[] getGenres() {
        return genres;
    }

    public String getArtist() {
        return artist;
    }

    public void setImgfile(MultipartFile imgfile) {
        this.imgfile = imgfile;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
