package org.ipan.cloudwatch.custom_metrics;

import java.time.Instant;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.MetricDatum;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;

@Configuration
public class Config {
	private final Logger logger = LoggerFactory.getLogger(Config.class);
	private CloudWatchClient cloudWatchClient;

	public Config(CloudWatchClient cloudWatchClient) {
		this.cloudWatchClient = cloudWatchClient;
	}

	@Scheduled(cron = "*/15 * * * * *")
	public void sendMetrics() {
		logger.info("Sending custom metrics to CloudWatch");
		Dimension dimension = Dimension.builder().name("UNIQUE_PAGES").value("URLS").build();

		MetricDatum datum = MetricDatum.builder()
			.metricName("PAGES_VISITED")
			.unit(StandardUnit.NONE)
			.value(new Random().nextDouble(80, 120))
			.timestamp(Instant.now())
			.dimensions(dimension)
			.build();
				
		PutMetricDataRequest putMetricDataRequest = PutMetricDataRequest.builder().namespace("[Cloud Watch][00]").metricData(datum).build();
		cloudWatchClient.putMetricData(putMetricDataRequest);
		logger.info("Custom metrics sent to CloudWatch");
	}
	
}
