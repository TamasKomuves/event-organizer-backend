package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.Address;

public class AddressDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("country")
	private String country;

	@JsonProperty("city")
	private String city;

	@JsonProperty("street")
	private String street;

	@JsonProperty("streetNumber")
	private String streetNumber;

	public static AddressDto fromEntity(Address address) {
		AddressDto dto = new AddressDto();

		dto.setId(address.getId());
		dto.setCountry(address.getCountry());
		dto.setCountry(address.getCity());
		dto.setStreet(address.getStreet());
		dto.setStreetNumber(address.getStreetNumber());

		return dto;
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
