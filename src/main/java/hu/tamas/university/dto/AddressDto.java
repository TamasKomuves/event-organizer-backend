package hu.tamas.university.dto;

import hu.tamas.university.entity.Address;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddressDto {

	private int id;

	@NotNull
	private String country;

	@NotNull
	private String city;

	@NotNull
	private String street;

	@NotNull
	private String streetNumber;

	public static AddressDto fromEntity(Address address) {
		AddressDto dto = new AddressDto();

		dto.setId(address.getId());
		dto.setCountry(address.getCountry());
		dto.setCity(address.getCity());
		dto.setStreet(address.getStreet());
		dto.setStreetNumber(address.getStreetNumber());

		return dto;
	}

	public static void updateFromDto(Address address, AddressDto addressDto) {
		address.setCountry(addressDto.getCountry());
		address.setCity(addressDto.getCity());
		address.setStreet(addressDto.getStreet());
		address.setStreetNumber(addressDto.getStreetNumber());
	}

	public static Address fromDto(AddressDto addressDto) {
		Address address = new Address();

		address.setId(addressDto.id);
		address.setCountry(addressDto.country);
		address.setCity(addressDto.city);
		address.setStreet(addressDto.street);
		address.setStreetNumber(addressDto.streetNumber);

		return address;
	}
}
