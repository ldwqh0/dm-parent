package com.dm.fileserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FileServerApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FileServerApplication.class, args);
    }

}
