package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.Invitation;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Timestamp getSentDate() {
		return sentDate;
	}

	public void setSentDate(Timestamp sentDate) {
		this.sentDate = sentDate;
	}

	public Timestamp getDecisionDate() {
		return decisionDate;
	}

	public void setDecisionDate(Timestamp decisionDate) {
		this.decisionDate = decisionDate;
	}

	public int getAccepted() {
		return accepted;
	}

	public void setAccepted(int accepted) {
		this.accepted = accepted;
	}

	public int getUserRequested() {
		return userRequested;
	}

	public void setUserRequested(int userRequested) {
		this.userRequested = userRequested;
	}
}
