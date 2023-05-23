package com.mytones.core.config;

import com.mytones.core.domain.file.File;
import com.mytones.core.domain.player.Artist;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
//@EnableS3Stores(basePackages = "com.mytones.core..storage")
public class StorageConfiguration {
//
//    private final String s3AccessId;
//    private final String s3AccessKey;
//    private final String s3Url;
//
//    public StorageConfiguration(@Value("${S3_ACCESS_ID:}") String s3AccessId,
//                                @Value("${S3_ACCESS_KEY:}") String s3AccessKey,
//                                @Value("${S3_URL:}") String s3Url) {
//        this.s3AccessId = s3AccessId;
//        this.s3AccessKey = s3AccessKey;
//        this.s3Url = s3Url;
//    }
//
//    @Bean
//    @Profile("!test")
//    public S3Client s3Client(AwsCredentialsProvider awsCredentialsProvider) throws URISyntaxException {
//        return S3Client.builder()
//                .credentialsProvider(awsCredentialsProvider)
//                .endpointOverride(new URI(s3Url))
//                .region(Region.US_EAST_1)
//                .build();
//    }
//
//    @Bean
//    @Profile("test")
//    public S3Client s3ClientTest() {
//        return Mockito.mock(S3Client.class);
//    }
//
//    @Bean
//    @Profile("!test")
//    public AwsCredentialsProvider awsCredentialsProvider() {
//        final var credentials = AwsBasicCredentials.create(s3AccessId, s3AccessKey);
//
//        return () -> credentials;
//    }
//
//    @Bean
//    @Profile("!test")
//    public S3StoreConfigurer configurer() {
//        return new S3StoreConfigurer() {
//
//            @Override
//            public void configureS3StoreConverters(ConverterRegistry registry) {
//                registry.addConverter(new Converter<File, S3ObjectId>() {
//                    @Override
//                    public S3ObjectId convert(File entity) {
//                        return new S3ObjectId(entity.bucket(), entity.getName());
//                    }
//                });
//
//                registry.addConverter(new Converter<Artist, S3ObjectId>() {
//                    @Override
//                    public S3ObjectId convert(Artist source) {
//                        return new S3ObjectId("images", "artist-" + source.getId());
//                    }
//                });
//            }
//        };
//    }


}
