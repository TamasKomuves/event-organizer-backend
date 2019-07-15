package hu.tamas.university.controller;

import hu.tamas.university.dto.AddressDto;
import hu.tamas.university.entity.Address;
import hu.tamas.university.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/addresses")
public class AddressController {

	private final AddressRepository addressRepository;

	@Autowired
	public AddressController(AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	@GetMapping("/{id}")
	@ResponseBody
	public AddressDto getAddressById(@PathVariable int id) {
		Address tmpAddress = addressRepository.findAddressById(id);

		return AddressDto.fromEntity(tmpAddress);
	}

	@PostMapping("/create")
	@ResponseBody
	public String createAddress(@RequestBody @Valid AddressDto addressDto) {
		Address address = AddressDto.fromDto(addressDto);
		address = addressRepository.save(address);

		return "{\"result\":\"success\", " + "\"addressId\":\"" + address.getId() + "\"}";
	}

	@GetMapping("/update/{addressId}/{country}/{city}/{street}/{streetNumber}")
	@ResponseBody
	public String updateAddress(@PathVariable int addressId, @PathVariable String country,
	                                   @PathVariable String city,
	                                   @PathVariable String street, @PathVariable String streetNumber) {
		Address address = addressRepository.findAddressById(addressId);

		if (address == null) {
			return "{\"result\":\"no such address\"}";
		}

		address.setCountry(country);
		address.setCity(city);
		address.setStreet(street);
		address.setStreetNumber(streetNumber);

		addressRepository.save(address);

		return "{\"result\":\"success\"}";
	}
}
