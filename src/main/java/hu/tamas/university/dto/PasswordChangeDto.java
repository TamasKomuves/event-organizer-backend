package hu.tamas.university.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PasswordChangeDto {

	@NotNull
	private String oldPassword;

	@NotNull
	private String password;

	@NotNull
	private String passwordAgain;
}
