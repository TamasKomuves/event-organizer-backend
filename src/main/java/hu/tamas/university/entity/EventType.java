package hu.tamas.university.entity;

import javax.persistence.*;

@Entity
@Table(name = "event_type")
public class EventType {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "type", nullable = false, unique = true)
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
