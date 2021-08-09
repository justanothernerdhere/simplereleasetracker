package com.an0nn30.releasetracker;

import com.an0nn30.releasetracker.entites.Release;
import com.an0nn30.releasetracker.services.ReleaseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/***
 * ReleaseTrackerApplication.java
 *
 * Main class for ReleaseTrackerApplication api.
 */
@SpringBootApplication
public class ReleaseTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReleaseTrackerApplication.class, args);
	}



	// DO NOT USE IN PRODUCTION
	// Test Code for local debugging
	public static Instant between(Instant startInclusive, Instant endExclusive) {
		long startSeconds = startInclusive.getEpochSecond();
		long endSeconds = endExclusive.getEpochSecond();
		long random = ThreadLocalRandom
				.current()
				.nextLong(startSeconds, endSeconds);

		return Instant.ofEpochSecond(random);
	}


	String[] statuses = {
			"Created",
			"In Development",
			"On DEV",
			"QA Done on DEV",
			"On staging",
			"QA done on STAGING",
			"On PROD",
			"Done",
	};

	// REMOVE IN PROD (obviously...)
	@Bean
	CommandLineRunner run(ReleaseService releaseService) {
		return args -> {
			for (int i = 0; i <= 1000; i++) {
				Instant yearAdvanced = Instant.now().plus(Duration.ofDays(365));
				Instant random = between(Instant.now(), yearAdvanced);

				Random r = new Random();
				int result = r.nextInt(7);
				releaseService.create(new Release(
						null,
						"release_" + i,
						"This is my AWESOME release number " + i,
						statuses[result],
						random.atZone(ZoneId.of("Europe/Belgrade")).toLocalDate(),
						null,
						null));
			}
		};


	}

}
