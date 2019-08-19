package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class NewsDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("date")
	private Timestamp date;

	@JsonProperty("type")
	private String type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
