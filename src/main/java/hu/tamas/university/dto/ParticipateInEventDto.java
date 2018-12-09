package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.ParticipateInEvent;
import hu.tamas.university.entity.User;

public class ParticipateInEventDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("eventId")
	private int eventId;

	@JsonProperty("userEmail")
	private String userEmail;

	public static ParticipateInEventDto fromEntity(ParticipateInEvent participateInEvent) {
		ParticipateInEventDto participateInEventDto = new ParticipateInEventDto();
		participateInEventDto.setId(participateInEvent.getId());
		if (participateInEvent.getEvent() != null)
			participateInEventDto.setEventId(participateInEvent.getEvent().getId());
		if (participateInEvent.getUser() != null)
			participateInEventDto.setUserEmail(participateInEvent.getUser().getEmail());
		return participateInEventDto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
