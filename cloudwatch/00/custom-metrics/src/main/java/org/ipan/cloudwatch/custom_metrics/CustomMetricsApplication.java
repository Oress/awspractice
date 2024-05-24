package org.ipan.cloudwatch.custom_metrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

@SpringBootApplication
@EnableScheduling
public class CustomMetricsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomMetricsApplication.class, args);
	}

	@Bean
	public CloudWatchClient cloudWatchClient() {
		return CloudWatchClient.create();
	}
}
