package hu.tamas.university;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.util.Map;

@Controller
@CrossOrigin("*")
public class SocketRest {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/send/message")
	public Map<String, String> broadcastNotification(String message) {
		final ObjectMapper mapper = new ObjectMapper();
		Map<String, String> messageConverted;
		try {
			messageConverted = mapper.readValue(message, Map.class);
		} catch (IOException e) {
			messageConverted = null;
		}
		if (messageConverted != null) {
			if (messageConverted.containsKey("toId") && messageConverted.get("toId") != null && !messageConverted
					.get("toId").equals("")) {
				this.simpMessagingTemplate
						.convertAndSend("/socket-publisher/" + messageConverted.get("toId"), messageConverted);
				this.simpMessagingTemplate
						.convertAndSend("/socket-publisher/" + messageConverted.get("fromId"), message);
			} else {
				this.simpMessagingTemplate.convertAndSend("/socket-publisher", messageConverted);
			}
		}
		return messageConverted;
	}
}
