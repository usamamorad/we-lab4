package twitter;

import java.util.Date;


/**
 * Wraps a status message to be pushed to Twitter
 * @author pl
 *
 */
public class TwitterStatusMessage {
	
	private String from;
	private String uuid;
	private Date dateTime;
	
	public TwitterStatusMessage(String from, String uuid, Date dateTime) {
		if (from == null)
			throw new IllegalArgumentException("From must not be null.");
		
		if (uuid == null)
			throw new IllegalArgumentException("UUID must not be null");
		
		if (dateTime == null)
			throw new IllegalArgumentException("DateTime must not be null");
				
		this.from = from;
		this.uuid = uuid;
		this.dateTime = dateTime;
	}
	
	/**
	 * Return the string to be published on Twitter
	 * @return
	 */
	public String getTwitterPublicationString() {
		StringBuffer sb = new StringBuffer();
		sb.append(dateTime).append(": " );		
		sb.append("User ").append(from).append(" publizierte folgende UUID am Highscoreboard: ");
		sb.append(uuid);
		return sb.toString().trim();
	}
	
}
