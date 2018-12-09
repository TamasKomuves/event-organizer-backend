package hu.tamas.university.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "lastname")
	private String lastName;

	@ManyToOne
	@JoinColumn(name="address_id")
	private Address address;

	@ManyToMany(mappedBy = "users")
	private List<Event> events;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}