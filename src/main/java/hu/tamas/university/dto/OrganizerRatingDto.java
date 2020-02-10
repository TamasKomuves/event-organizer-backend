package hu.tamas.university.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrganizerRatingDto {

	private int numberOfRatings;
	private double averageRating;

	public OrganizerRatingDto(int numberOfRatings, double averageRating) {
		this.numberOfRatings = numberOfRatings;
		this.averageRating = averageRating;
	}
}
