package hu.tamas.university.dto;

import hu.tamas.university.entity.EventType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventTypeDto {

	private String type;

	public static EventTypeDto fromEntity(EventType eventType) {
		EventTypeDto dto = new EventTypeDto();
		dto.setType(eventType.getType());
		return dto;
	}
}
