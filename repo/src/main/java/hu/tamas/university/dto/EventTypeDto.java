package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.EventType;

public class EventTypeDto {

	@JsonProperty("type")
	private String type;

	public static EventTypeDto fromEntity(EventType eventType) {
		EventTypeDto dto = new EventTypeDto();
		dto.setType(eventType.getType());
		return dto;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
