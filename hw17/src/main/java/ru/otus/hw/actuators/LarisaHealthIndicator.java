package ru.otus.hw.actuators;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class LarisaHealthIndicator implements HealthIndicator {

	@Override
	public Health health() {
		LocalDate now = LocalDate.now();
		DayOfWeek dayOfWeek = now.getDayOfWeek();
		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			return Health.down()
				.status(Status.DOWN)
				.withDetail("message", "У Ларисы выходной")
				.build();
		} else {
			return Health.up()
				.withDetail("message", "Лариса работает!")
				.build();
		}
	}
}
