package hu.tamas.university.dto;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDto {

	@NotNull
	private String email;

	@NotNull
	private String password;

	@NotNull
	private String firstname;

	@NotNull
	private String lastname;

	@NotNull
	private String country;

	@NotNull
	private String city;

	@NotNull
	private String street;

	@NotNull
	private String streetNumber;
}
