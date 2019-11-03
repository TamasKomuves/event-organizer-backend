package hu.tamas.university.dto;

import hu.tamas.university.entity.Event;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Getter
@Setter
public class EventDto {

	private int id;

	private String name;

	private String description;

	private int maxParticipant;

	private String visibility;

	private int totalCost;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Timestamp eventDate;

	private int addressId;

	private String eventType;

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
