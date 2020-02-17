package hu.tamas.university.config.security.date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

import static com.google.common.base.Preconditions.checkNotNull;

final class JodaDateService implements DateService {

	private final DateTimeZone timeZone;

	JodaDateService(final DateTimeZone timeZone) {
		super();
		this.timeZone = checkNotNull(timeZone);

		System.setProperty("user.timezone", timeZone.getID());
		TimeZone.setDefault(timeZone.toTimeZone());
		DateTimeZone.setDefault(timeZone);
	}

	@Override
	public DateTime now() {
		return DateTime.now(timeZone);
	}
}
