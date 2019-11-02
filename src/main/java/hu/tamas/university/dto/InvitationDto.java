package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.Invitation;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
public class InvitationDto {

	@NotNull
	private int id;

	private int eventId;

	@NotNull
	private String userEmail;

	private Timestamp sentDate;

	private Timestamp decisionDate;

	@JsonProperty("isAccepted")
	private int accepted;

	@NotNull
	@JsonProperty("isUserRequested")
	private int userRequested;

	public static InvitationDto fromEntity(Invitation invitation) {
		InvitationDto invitationDto = new InvitationDto();
		invitationDto.setId(invitation.getId());
		if (invitation.getEvent() != null) {
			invitationDto.setEventId(invitation.getEvent().getId());
		}
		if (invitation.getUser() != null) {
			invitationDto.setUserEmail(invitation.getUser().getEmail());
		}
		invitationDto.setSentDate(invitation.getSentDate());
		invitationDto.setDecisionDate(invitation.getDecisionDate());
		invitationDto.setAccepted(invitation.getAccepted());
		invitationDto.setUserRequested(invitation.getUserRequested());
		return invitationDto;
	}
}
