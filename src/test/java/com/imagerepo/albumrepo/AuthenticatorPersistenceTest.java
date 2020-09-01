package com.imagerepo.albumrepo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticatorPersistenceTest {
    @Autowired AlbumRepo albumRepo;
    private final Genre[] genres = new Genre[]{Genre.Pop, Genre.Jazz};
    private final AlbumModel albumModelToSave = new AlbumModel("testimg", "testalbum",
            "test description", new byte[0], Arrays.asList(genres), ".png", "testartist" );
    @Before
    public void clearDB() {
        albumRepo.deleteAll();
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
}
