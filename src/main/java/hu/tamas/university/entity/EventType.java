package hu.tamas.university.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "event_type")
public class EventType {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "type", nullable = false, unique = true)
	private String type;

	@OneToMany(mappedBy = "event_type", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Event> events;

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
