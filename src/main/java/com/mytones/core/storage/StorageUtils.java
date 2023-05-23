package com.mytones.core.storage;

import com.mytones.core.domain.player.Album;
import com.mytones.core.domain.player.Artist;
import com.mytones.core.domain.player.Playlist;
import com.mytones.core.domain.player.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.Base64;

@Component
public class StorageUtils {

//    private final S3Client s3Client;
//    private final String s3PublicPath;

//    public StorageUtils(S3Client s3Client, @Value("${S3_PUBLIC_PATH:http://localhost:9000}") String s3PublicPath) {
//        this.s3Client = s3Client;
//        if (!s3PublicPath.endsWith("/")) {
//            s3PublicPath += "/";
//        }
//        this.s3PublicPath = s3PublicPath;
//    }

//    public String generateUrl(String bucket, String key) {
//        return s3PublicPath + bucket + "/" + key;
//    }

    @Transactional
    public void uploadArtistImage(final Artist artist, final String content) {
//        final String bucket = "images";
//        final String key = "artist-" + artist.getId() + ".jpg";
//
//        upload(bucket, key, content);
//        artist.setImageUrl(generateUrl(bucket, key));
        artist.setImageUrl(content);
    }

    @Transactional
    public void uploadAlbumImage(final Album album, final String content) {
//        final String bucket = "images";
//        final String key = "album-" + album.getId() + ".jpg";
//
//        upload(bucket, key, content);
        album.setImageUrl(content);
    }

    @Transactional
    public void uploadTrack(final Track track, final String content) {
//        final String bucket = "tracks";
//        final String key = track.getName() + track.getId() + ".mp3";
//
//        upload(bucket, key, content);
        track.setUrl(content);
    }

    @Transactional
    public void uploadPlaylistImage(final Playlist playlist, final String content) {
//        final String bucket = "images";
//        final String key = "playlist-" + playlist.getId() + ".jpg";
//
//        upload(bucket, key, content);
        playlist.setImageUrl(content);
    }


//    @Transactional
    public void deleteArtistImage(final Artist artist) {
//        final String bucket = "images";
//        final String key = "artist-" + artist.getId() + ".jpg";
//
//        delete(bucket, key);
    }

//    @Transactional
    public void deletePlaylistImage(final Playlist playlist) {
//        final String bucket = "images";
//        final String key = "playlist-" + playlist.getId() + ".jpg";
//
//        delete(bucket, key);
    }

    private void delete(final String bucket, final String key) {
//        var request = DeleteObjectRequest.builder()
//                .bucket(bucket)
//                .key(key)
//                .build();
//
//        s3Client.deleteObject(request);
    }

    private void upload(final String bucket, final String key, final String content) {
//        var request = PutObjectRequest.builder()
//                .bucket(bucket)
//                .key(key)
//                .build();
//        s3Client.putObject(request, RequestBody.fromBytes(Base64.getDecoder().decode(content)));
    }
}
