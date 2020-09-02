package com.imagerepo.albumrepo;

import org.springframework.web.multipart.MultipartFile;

public class UploadAlbumWrapper {
    private MultipartFile imgfile;
    private String title;
    private String description;
    private Genre[] genres;
    private String artist;
}
