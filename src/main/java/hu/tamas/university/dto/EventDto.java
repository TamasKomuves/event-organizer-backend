package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.EventType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Getter
@Setter
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
}
