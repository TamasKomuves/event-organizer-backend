package hu.tamas.university.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "event_type")
public class EventType {

	@Id
	@Column(name = "type", nullable = false, unique = true)
	private String type;

	@OneToMany(mappedBy = "eventType", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Event> events = new HashSet<>();

	public EventType(String type) {
		this.type = type;
	}

	public EventType() {
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof EventType)) {
			return false;
		}
		return type.equals(((EventType) o).type);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type);
	}

	public void addEvent(Event event) {
		events.add(event);
		event.setEventType(this);
	}
}
