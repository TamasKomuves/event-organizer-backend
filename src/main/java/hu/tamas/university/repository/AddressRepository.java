package hu.tamas.university.repository;

import hu.tamas.university.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {

	Address findAddressById(int id);
}
