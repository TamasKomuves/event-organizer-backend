package hu.tamas.university.service.security;

import com.google.common.collect.ImmutableMap;
import hu.tamas.university.config.security.date.DateService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.impl.TextCodec.BASE64;
import static java.util.Objects.requireNonNull;

@Service
final class JWTTokenService implements Clock, TokenService {

	private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

	private final DateService dates;
	private final String issuer;
	private final int expirationSec;
	private final int clockSkewSec;
	private final String secretKey;

	JWTTokenService(final DateService dates, @Value("${jwt.issuer:octoperf}") final String issuer,
			@Value("${jwt.expiration-sec:54000}") final int expirationSec,
			@Value("${jwt.clock-skew-sec:300}") final int clockSkewSec,
			@Value("${jwt.secret:secret}") final String secret) {
		super();
		this.dates = requireNonNull(dates);
		this.issuer = requireNonNull(issuer);
		this.expirationSec = expirationSec;
		this.clockSkewSec = clockSkewSec;
		this.secretKey = BASE64.encode(requireNonNull(secret));
	}

	@Override
	public String expiring(final Map<String, String> attributes) {
		return newToken(attributes, expirationSec);
	}

	private String newToken(final Map<String, String> attributes, final int expiresInSec) {
		final DateTime now = dates.now();
		final Claims claims = Jwts
				.claims()
				.setIssuer(issuer)
				.setIssuedAt(now.toDate());

		if (expiresInSec > 0) {
			final DateTime expiresAt = now.plusSeconds(expiresInSec);
			claims.setExpiration(expiresAt.toDate());
		}
		claims.putAll(attributes);

		return Jwts
				.builder()
				.setClaims(claims)
				.signWith(HS256, secretKey)
				.compressWith(COMPRESSION_CODEC)
				.compact();
	}

	@Override
	public Map<String, String> verify(final String token) {
		final JwtParser parser = Jwts
				.parser()
				.requireIssuer(issuer)
				.setClock(this)
				.setAllowedClockSkewSeconds(clockSkewSec)
				.setSigningKey(secretKey);
		return parseClaims(() -> parser.parseClaimsJws(token).getBody());
	}

	private static Map<String, String> parseClaims(final Supplier<Claims> toClaims) {
		try {
			final Claims claims = toClaims.get();
			final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
			for (final Map.Entry<String, Object> e : claims.entrySet()) {
				builder.put(e.getKey(), String.valueOf(e.getValue()));
			}
			return builder.build();
		} catch (final IllegalArgumentException | JwtException e) {
			return ImmutableMap.of();
		}
	}

	@Override
	public Date now() {
		final DateTime now = dates.now();
		return now.toDate();
	}
}
