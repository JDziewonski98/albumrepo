package com.imagerepo.albumrepo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticatorPersistenceTest {
    @Autowired AlbumRepo albumRepo;
    private final Genre[] genres = new Genre[]{Genre.Pop, Genre.Jazz};
    private final Genre[] genres2 = new Genre[]{Genre.Metal};
    private final AlbumModel albumModelToSave = new AlbumModel("testimg", "testalbum",
            "test description", new byte[0], Arrays.asList(genres), ".png", "testartist" );
    @Before
    public void clearDB() {
        albumRepo.deleteAll();
    }

    @Test
    public void testCreateAlbum() {
        byte[] b = new byte[20];
        new Random().nextBytes(b);
        AlbumModel testAlbum = new AlbumModel("testimg", "testalbum",
                "test description", b, Arrays.asList(genres), ".png", "testartist" );
        albumRepo.save(testAlbum);
        Optional<AlbumModel> albumFromDB = albumRepo.findByTitle("testalbum");
        Assert.assertTrue(albumFromDB.isPresent());
        AlbumModel albumFromDBNonOptional = albumFromDB.get();
        Assert.assertEquals(albumFromDBNonOptional.getArtist(), albumFromDBNonOptional.getArtist());
        this.compareEntities(testAlbum, albumFromDBNonOptional);
    }

    @Test
    public void testSearchByMatchingString() {
        this.createVariousAlbums();
        HashSet<AlbumModel> albums = albumRepo.findByMatchingString("Beautiful");
        Assert.assertEquals(2, albums.size());
        albums = albumRepo.findByMatchingString("Big Boss");
        Assert.assertEquals(2, albums.size());
        albums = albumRepo.findByMatchingString("funk");
        Assert.assertEquals(1, albums.size());
        albums = albumRepo.findByMatchingString("avant garde");
        Assert.assertEquals(0, albums.size());
        albums = albumRepo.findByMatchingString("non-existent");
        Assert.assertEquals(0, albums.size());
    }

    @Test
    public void testSearchByGenre() {
        this.createVariousAlbums();

        HashSet<AlbumModel> genreSet = albumRepo.findByMatchingAnyGenre(Genre.Metal);
        Assert.assertEquals(1, genreSet.size());
        genreSet = albumRepo.findByMatchingAnyGenre(Genre.Jazz);
        Assert.assertEquals(3, genreSet.size());
        genreSet = albumRepo.findByMatchingAnyGenre(Genre.Classical);
        Assert.assertEquals(0, genreSet.size());

    }

    private void compareEntities(AlbumModel a1, AlbumModel a2) {
        Assert.assertEquals(a1.getArtist(), a2.getArtist());
        Assert.assertEquals(a1.getDescription(), a2.getDescription());
        Assert.assertEquals(a1.getFilename(), a2.getFilename());
        Assert.assertEquals(a1.getId(), a2.getId());
        Assert.assertArrayEquals(a1.getPicture(), a2.getPicture());
        Assert.assertEquals(a1.getTitle(), a2.getTitle());
        Assert.assertEquals(a1.getType(), a2.getType());
        String a1genres =  a1.getGenres().toString();
        String a2genres =  a2.getGenres().toString();
        Assert.assertEquals(a1genres, a2genres);
    }

    private void createVariousAlbums() {
        AlbumModel beautifulAlbum = new AlbumModel("testimg", "beautiful music",
                "test description", this.getRandomByteArray(), Arrays.asList(genres),
                ".png", "big boss" );
        albumRepo.save(beautifulAlbum);
        AlbumModel sadAlbum = new AlbumModel("testimg", "such sad songs",
                "a compilation of tunes that will make you weep", this.getRandomByteArray(), Arrays.asList(genres),
                ".png", "jonathan l" );
        albumRepo.save(sadAlbum);
        AlbumModel heavyAlbum = new AlbumModel("testimg", "metal",
                "beautiful and brutal", this.getRandomByteArray(), Arrays.asList(genres2),
                ".png", "magnetica" );
        albumRepo.save(heavyAlbum);
        AlbumModel funkyAlbum = new AlbumModel("testimg", "funk all day",
                "sure to get you moving", this.getRandomByteArray(), Arrays.asList(genres),
                ".png", "big boss" );
        albumRepo.save(funkyAlbum);

    }

    private byte[] getRandomByteArray() {
        byte[] b = new byte[20];
        new Random().nextBytes(b);
        return b;
    }
}
