package hu.tamas.university.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserLoginDto {

	@NotNull
	private String email;

	@NotNull
	private String password;
}
