package hu.tamas.university.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeDto {

	private String oldPassword;

	private String password;

	private String passwordAgain;
}
