package hu.tamas.university.service.security;

import java.util.Map;

public interface TokenService {

	String expiring(Map<String, String> attributes);

	Map<String, String> verify(String token);
}
