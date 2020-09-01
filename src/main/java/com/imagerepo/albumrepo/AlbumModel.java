package com.imagerepo.albumrepo;

import javax.persistence.*;

@Entity
@Table(name = "album_table")
public class AlbumModel {

    public AlbumModel() {
        super();
    }

    public AlbumModel(String filename, String title, String description, byte[] picture,
                      Genre[] genres, String type, String artist) {
        this.filename = filename;
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.genres = genres;
        this.type = type;
        this.artist = artist;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "picture", length = 1000)
    private byte[] picture;

    @Column(name = "genres")
    private Genre[] genres;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Column(name = "artist")
    private String artist;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "type")
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Genre[] getGenres() {
        return genres;
    }

    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
