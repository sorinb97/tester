package com.license.tester;

import com.license.tester.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class TesterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TesterApplication.class, args);
    }

}
