package com.mytones.core.storage;

import com.mytones.core.domain.player.Album;
import com.mytones.core.domain.player.Artist;
import com.mytones.core.domain.player.Playlist;
import com.mytones.core.domain.player.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class StorageUtils {

    private final S3Client s3Client;

    public String generateUrl(String bucket, String key) {
        var request = GetUrlRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        return s3Client.utilities().getUrl(request).toString();
    }

    @Transactional
    public void uploadArtistImage(final Artist artist, final String content) {
        final String bucket = "images";
        final String key = "artist-" + artist.getId() + ".jpg";

        upload(bucket, key, content);
        artist.setImageUrl(generateUrl(bucket, key));
    }

    @Transactional
    public void uploadAlbumImage(final Album album, final String content) {
        final String bucket = "images";
        final String key = "album-" + album.getId() + ".jpg";

        upload(bucket, key, content);
        album.setImageUrl(generateUrl(bucket, key));
    }

    @Transactional
    public void uploadTrack(final Track track, final String content) {
        final String bucket = "tracks";
        final String key = track.getName() + track.getId() + ".mp3";

        upload(bucket, key, content);
        track.setUrl(generateUrl(bucket, key));
    }

    @Transactional
    public void uploadPlaylistImage(final Playlist playlist, final String content) {
        final String bucket = "images";
        final String key = "playlist-" + playlist.getId() + ".jpg";

        upload(bucket, key, content);
        playlist.setImageUrl(generateUrl(bucket, key));
    }


    @Transactional
    public void deleteArtistImage(final Artist artist) {
        final String bucket = "images";
        final String key = "artist-" + artist.getId() + ".jpg";

        delete(bucket, key);
    }

    @Transactional
    public void deletePlaylistImage(final Playlist playlist) {
        final String bucket = "images";
        final String key = "playlist-" + playlist.getId() + ".jpg";

        delete(bucket, key);
    }

    private void delete(final String bucket, final String key) {
        var request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(request);
    }

    private void upload(final String bucket, final String key, final String content) {
        var request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.putObject(request, RequestBody.fromBytes(Base64.getDecoder().decode(content)));
    }
}
