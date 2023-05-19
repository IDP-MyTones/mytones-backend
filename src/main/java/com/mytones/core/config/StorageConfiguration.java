package com.mytones.core.config;

import com.mytones.core.domain.file.File;
import com.mytones.core.domain.player.Artist;
import org.springframework.content.s3.S3ObjectId;
import org.springframework.content.s3.config.EnableS3Stores;
import org.springframework.content.s3.config.S3StoreConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@EnableS3Stores(basePackages = "com.mytones.core..storage")
public class StorageConfiguration {

    @Bean
    public S3Client s3Client(AwsCredentialsProvider awsCredentialsProvider) throws URISyntaxException {

        return S3Client.builder()
                .credentialsProvider(awsCredentialsProvider)
                .endpointOverride(new URI("http://localhost:9000"))
                .region(Region.US_EAST_1)
                .build();
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        final var credentials = AwsBasicCredentials.create("foTSnWtOkQGVGBJG", "ckrFiWZxTsi9c2ay6tJvdTeI6IP3ehjO");

        return () -> credentials;
    }

    @Bean
    public S3StoreConfigurer configurer() {
        return new S3StoreConfigurer() {

            @Override
            public void configureS3StoreConverters(ConverterRegistry registry) {
                registry.addConverter(new Converter<File, S3ObjectId>() {
                    @Override
                    public S3ObjectId convert(File entity) {
                        return new S3ObjectId(entity.bucket(), entity.getName());
                    }
                });

                registry.addConverter(new Converter<Artist, S3ObjectId>() {
                    @Override
                    public S3ObjectId convert(Artist source) {
                        return new S3ObjectId("images", "artist-" + source.getId());
                    }
                });
            }
        };
    }


}
