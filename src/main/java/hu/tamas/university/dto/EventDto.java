package hu.tamas.university.dto;

import hu.tamas.university.entity.Address;
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

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Timestamp eventDate;

	private int addressId;

	private String eventType;

	private String organizerEmail;

	private String country;

	private String city;

	public static EventDto fromEntity(Event event) {
		final EventDto eventDto = new EventDto();
		final Address address = event.getAddress();

		eventDto.setId(event.getId());
		eventDto.setName(event.getName());
		eventDto.setDescription(event.getDescription());
		eventDto.setMaxParticipant(event.getMaxParticipant());
		eventDto.setVisibility(event.getVisibility());
		eventDto.setEventDate(event.getEventDate());
		if (address != null) {
			eventDto.setAddressId(address.getId());
			eventDto.setCountry(address.getCountry());
			eventDto.setCity(address.getCity());
		}
		if (event.getEventType() != null) {
			eventDto.setEventType(event.getEventType().getType());
		}
		if (event.getOrganizer() != null) {
			eventDto.setOrganizerEmail(event.getOrganizer().getEmail());
		}

		return eventDto;
	}
}
