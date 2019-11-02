package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class NewsDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("date")
	private Timestamp date;

	@JsonProperty("type")
	private String type;
}
