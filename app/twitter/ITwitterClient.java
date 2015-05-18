package twitter;


/**
 * Interface for Twitter clients
 * @author pl
 *
 */
public interface ITwitterClient {
	public void publishUuid(TwitterStatusMessage message) throws Exception;	
}
