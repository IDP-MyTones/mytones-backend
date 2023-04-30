package com.mytones.core.config;

import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.content.fs.config.FilesystemStoreConfigurer;
import org.springframework.content.fs.io.FileSystemResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
@EnableFilesystemStores(basePackages = "com.mytones.core..storage")
public class StorageConfiguration {
    @Bean
    File filesystemRoot() throws IOException {
        return Files.createDirectories(Paths.get(".storage")).toFile();
    }

    @Bean
    FileSystemResourceLoader fileSystemResourceLoader() throws IOException {
        return new FileSystemResourceLoader(filesystemRoot().getAbsolutePath());
    }

    @Bean
    FilesystemStoreConfigurer configurer() {
        return new FilesystemStoreConfigurer() {

            @Override
            public void configureFilesystemStoreConverters(ConverterRegistry registry) {
                registry.addConverter(new Converter<com.mytones.core.domain.file.File, String>() {

                    @Override
                    public String convert(com.mytones.core.domain.file.File file) {
                        return file.getClass().getSimpleName() + "-" + file.getId() + "-" + file.getName();
                    }
                });
            }
        };
    }

}
