package org.kakia.goodreads;

import java.nio.file.Path;

import org.kakia.goodreads.config.DataStaxAstraConnectionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraConnectionProperties.class)
public class GoodReadsApplication {
	
	private static final Logger  LOGGER = LoggerFactory.getLogger(GoodReadsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GoodReadsApplication.class, args);
	}
	
	/**
     * This is necessary to have the Spring Boot app use the Astra secure bundle 
     * to connect to the database
     */
	@Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraConnectionProperties astraProperties) {
    	Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
	}


}
