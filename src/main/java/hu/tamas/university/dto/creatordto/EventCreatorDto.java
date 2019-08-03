package hu.tamas.university.dto.creatordto;

import hu.tamas.university.entity.Address;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.EventType;
import hu.tamas.university.entity.User;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class EventCreatorDto {

	@NotNull
	private String name;

	@NotNull
	private String description;

	@NotNull
	private int maxParticipant;

	@NotNull
	private String visibility;

	@NotNull
	private int totalCost;

	@NotNull
	private Timestamp eventDate;

	@NotNull
	private int addressId;

	@NotNull
	private String eventTypeType;

	@NotNull
	private String organizerEmail;

	public static Event fromDto(EventCreatorDto eventCreatorDto, Address address, EventType eventType, User user) {
		Event event = new Event();

		event.setName(eventCreatorDto.getName());
		event.setDescription(eventCreatorDto.getDescription());
		event.setMaxParticipant(eventCreatorDto.getMaxParticipant());
		event.setVisibility(eventCreatorDto.getVisibility());
		event.setTotalCost(eventCreatorDto.getTotalCost());
		event.setEventDate(eventCreatorDto.getEventDate());
		event.setAddress(address);
		event.setEventType(eventType);
		event.setOrganizer(user);

		return event;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMaxParticipant() {
		return maxParticipant;
	}

	public void setMaxParticipant(int maxParticipant) {
		this.maxParticipant = maxParticipant;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}

	public Timestamp getEventDate() {
		return eventDate;
	}

	public void setEventDate(Timestamp eventDate) {
		this.eventDate = eventDate;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getEventTypeType() {
		return eventTypeType;
	}

	public void setEventTypeType(String eventTypeType) {
		this.eventTypeType = eventTypeType;
	}

	public String getOrganizerEmail() {
		return organizerEmail;
	}

	public void setOrganizerEmail(String organizerEmail) {
		this.organizerEmail = organizerEmail;
	}
}
