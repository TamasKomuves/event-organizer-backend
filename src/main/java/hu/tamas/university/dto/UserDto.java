package hu.tamas.university.dto;

import hu.tamas.university.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

	private String firstName;

	private String lastName;

	private int addressId;

	private String email;

	public static UserDto fromEntity(User user) {
		UserDto dto = new UserDto();
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setEmail(user.getEmail());
		if (user.getAddress() != null)
			dto.setAddressId(user.getAddress().getId());
		return dto;
	}
}
