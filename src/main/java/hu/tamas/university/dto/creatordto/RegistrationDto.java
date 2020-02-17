package hu.tamas.university.dto.creatordto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

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
