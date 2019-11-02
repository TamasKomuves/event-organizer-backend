package hu.tamas.university.entity;

import com.google.common.collect.Sets;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "event_type")
public class EventType {

	@Id
	@Column(name = "type", nullable = false, unique = true)
	private String type;

	@OneToMany(mappedBy = "eventType", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Event> events = Sets.newHashSet();

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

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}
}
