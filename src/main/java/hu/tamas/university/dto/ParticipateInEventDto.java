package hu.tamas.university.dto;

import hu.tamas.university.entity.ParticipateInEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipateInEventDto {

	private int id;

	private int eventId;

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
}
