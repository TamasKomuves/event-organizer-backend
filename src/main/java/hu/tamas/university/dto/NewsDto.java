package hu.tamas.university.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class NewsDto {

	private int id;

	private Timestamp date;

	private String type;
}
