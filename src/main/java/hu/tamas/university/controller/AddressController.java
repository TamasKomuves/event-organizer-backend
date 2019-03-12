package hu.tamas.university.controller;

import hu.tamas.university.dto.AddressDto;
import hu.tamas.university.entity.Address;
import hu.tamas.university.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/addresses")
public class AddressController {

	private final AddressRepository addressRepository;

	@Autowired
	public AddressController(AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	@GetMapping("/{id}")
	public @ResponseBody
	ResponseEntity<AddressDto> getAddressById(@PathVariable int id) {
		Address tmpAddress = addressRepository.findAddressById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>(AddressDto.fromEntity(tmpAddress), headers, HttpStatus.OK);
	}

	@GetMapping("/create/{country}/{city}/{street}/{streetNumber}")
	public @ResponseBody
	ResponseEntity<String> saveAddress(@PathVariable String country, @PathVariable String city,
	                                   @PathVariable String street, @PathVariable String streetNumber) {
		Address address = new Address();
		address.setCountry(country);
		address.setCity(city);
		address.setStreet(street);
		address.setStreetNumber(streetNumber);

		address = addressRepository.save(address);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");

		return new ResponseEntity<>("{\"result\":\"success\", " +
				"\"addressId\":\"" + address.getId() + "\"}", headers,
				HttpStatus.OK);
	}
}
