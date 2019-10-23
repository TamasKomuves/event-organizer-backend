package hu.tamas.university.entity;

import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "event_type")
public class EventType {

	@Id
	@Column(name = "type", nullable = false, unique = true)
	private String type;

	@OneToMany(mappedBy = "eventType", cascade = CascadeType.ALL)
	private List<Event> events = Lists.newArrayList();

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

	public void removeEvent(Event event) {
		events.remove(event);
		event.setEventType(null);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
}
