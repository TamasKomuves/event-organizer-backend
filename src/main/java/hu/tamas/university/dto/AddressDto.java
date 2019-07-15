package hu.tamas.university.dto;

import hu.tamas.university.entity.Address;

import javax.validation.constraints.NotNull;

public class AddressDto {

	@NotNull
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

	public static Address fromDto(AddressDto addressDto) {
		Address address = new Address();

		address.setId(addressDto.id);
		address.setCountry(addressDto.country);
		address.setCity(addressDto.city);
		address.setStreet(addressDto.street);
		address.setStreetNumber(addressDto.streetNumber);

		return address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
}
