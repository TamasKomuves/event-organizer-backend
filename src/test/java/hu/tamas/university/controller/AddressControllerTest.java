package hu.tamas.university.controller;

import hu.tamas.university.dto.AddressDto;
import hu.tamas.university.entity.Address;
import hu.tamas.university.repository.AddressRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddressControllerTest {

	private static final int ADDRESS_ID = 6;
	private static final String RESULT_SUCCESS = "{\"result\":\"success\"}";

	@Mock
	private AddressRepository addressRepository;
	@Mock
	private Address address;

	private AddressController addressController;

	@Before
	public void setUp() {
		addressController = new AddressController(addressRepository);
	}

	@Test
	public void testGetAddressById() {
		when(addressRepository.findAddressById(ADDRESS_ID)).thenReturn(address);
		when(address.getId()).thenReturn(ADDRESS_ID);

		final AddressDto result = addressController.getAddressById(ADDRESS_ID);

		assertEquals(ADDRESS_ID, result.getId());
	}

	@Test
	public void testUpdateAddress() {
		when(addressRepository.findAddressById(ADDRESS_ID)).thenReturn(address);
		final String country = "country";
		final String city = "city";
		final String street = "street";
		final String streetNumber = "streetNumber";

		final String result = addressController
				.updateAddress(ADDRESS_ID, country, city, street, streetNumber);

		assertEquals(RESULT_SUCCESS, result);
		verify(address).setCountry(country);
		verify(address).setCity(city);
		verify(address).setStreet(street);
		verify(address).setStreetNumber(streetNumber);
		verify(addressRepository).save(address);
	}

	@Test
	public void testUpdateAddress_NoSuchAddress() {
		when(addressRepository.findAddressById(ADDRESS_ID)).thenReturn(null);
		final String country = "country";
		final String city = "city";
		final String street = "street";
		final String streetNumber = "streetNumber";

		final String result = addressController
				.updateAddress(ADDRESS_ID, country, city, street, streetNumber);

		assertEquals("{\"result\":\"no such address\"}", result);
	}
}