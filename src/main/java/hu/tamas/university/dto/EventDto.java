package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.EventType;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

public class EventDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description;

	@JsonProperty("maxParticipant")
	private int maxParticipant;

	@JsonProperty("visibility")
	private String visibility;

	@JsonProperty("totalCost")
	private int totalCost;

	@JsonProperty("eventDate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Timestamp eventDate;

	@JsonProperty("addressId")
	private int addressId;

	@JsonProperty("eventType")
	private String eventType;

	@JsonProperty("organizerEmail")
	private String organizerEmail;

	public static EventDto fromEntity(Event event) {
		EventDto eventDto = new EventDto();

		eventDto.setId(event.getId());
		eventDto.setName(event.getName());
		eventDto.setDescription(event.getDescription());
		eventDto.setMaxParticipant(event.getMaxParticipant());
		eventDto.setVisibility(event.getVisibility());
		eventDto.setTotalCost(event.getTotalCost());
		eventDto.setEventDate(event.getEventDate());
		if (event.getAddress() != null)
			eventDto.setAddressId(event.getAddress().getId());
		if (event.getEventType() != null)
			eventDto.setEventType(event.getEventType().getType());
		if (event.getOrganizer() != null)
			eventDto.setOrganizerEmail(event.getOrganizer().getEmail());

		return eventDto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getOrganizerEmail() {
		return organizerEmail;
	}

	public void setOrganizerEmail(String organizerEmail) {
		this.organizerEmail = organizerEmail;
	}

	public Timestamp getEventDate() {
		return eventDate;
	}

	public void setEventDate(Timestamp eventDate) {
		this.eventDate = eventDate;
	}
}
