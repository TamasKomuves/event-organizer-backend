package hu.tamas.university.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.tamas.university.entity.Invitation;

import java.sql.Timestamp;

public class InvitationDto {

	@JsonProperty("id")
	private int id;

	@JsonProperty("eventId")
	private int eventId;

	@JsonProperty("userEmail")
	private String userEmail;

	@JsonProperty("sentDate")
	private Timestamp sentDate;

	@JsonProperty("decisionDate")
	private Timestamp decisionDate;

	@JsonProperty("isAccepted")
	private int isAccepted;

	@JsonProperty("isUserRequested")
	private int isUserRequested;

	public static InvitationDto fromEntity(Invitation invitation) {
		InvitationDto invitationDto = new InvitationDto();
		invitationDto.setId(invitation.getId());
		if (invitation.getEvent() != null)
			invitationDto.setEventId(invitation.getEvent().getId());
		if (invitation.getUser() != null)
			invitationDto.setUserEmail(invitation.getUser().getEmail());
		invitationDto.setSentDate(invitation.getSentDate());
		invitationDto.setDecisionDate(invitation.getDecisionDate());
		invitationDto.setAccepted(invitation.isAccepted());
		invitationDto.setUserRequested(invitation.isUserRequested());
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

	public int isAccepted() {
		return isAccepted;
	}

	public void setAccepted(int accepted) {
		isAccepted = accepted;
	}

	public int isUserRequested() {
		return isUserRequested;
	}

	public void setUserRequested(int userRequested) {
		isUserRequested = userRequested;
	}
}
