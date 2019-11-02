package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.EventType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventTypeDto {

	@JsonProperty("type")
	private String type;

	public static EventTypeDto fromEntity(EventType eventType) {
		EventTypeDto dto = new EventTypeDto();
		dto.setType(eventType.getType());
		return dto;
	}
}
