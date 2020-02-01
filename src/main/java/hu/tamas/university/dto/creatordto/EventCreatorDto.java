package hu.tamas.university.dto.creatordto;

import hu.tamas.university.dto.AddressDto;
import hu.tamas.university.entity.Address;
import hu.tamas.university.entity.Event;
import hu.tamas.university.entity.EventType;
import hu.tamas.university.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
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
	private Timestamp eventDate;

	@NotNull
	private String eventType;

	@NotNull
	private AddressDto address;

	public static Event fromDto(EventCreatorDto eventCreatorDto, Address address, EventType eventType, User user) {
		final Event event = new Event();
		event.setName(eventCreatorDto.getName());
		event.setDescription(eventCreatorDto.getDescription());
		event.setMaxParticipant(eventCreatorDto.getMaxParticipant());
		event.setVisibility(eventCreatorDto.getVisibility());
		event.setEventDate(eventCreatorDto.getEventDate());

		event.setAddress(address);
		eventType.addEvent(event);
		user.addOrganizedEvent(event);

		return event;
	}

	public static void updateInfoFromDto(Event event, EventCreatorDto eventCreatorDto) {
		event.setName(eventCreatorDto.getName());
		event.setDescription(eventCreatorDto.getDescription());
		event.setVisibility(eventCreatorDto.getVisibility());
		event.setEventDate(eventCreatorDto.getEventDate());
		AddressDto.updateFromDto(event.getAddress(), eventCreatorDto.getAddress());
	}
}
