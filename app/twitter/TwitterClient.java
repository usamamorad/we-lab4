package twitter;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Andreea on 23/5/2015.
 */
public class TwitterClient implements ITwitterClient {

    private final String CONSUMER_KEY
            = "GZ6tiy1XyB9W0P4xEJudQ";
    private final String CONSUMER_SECRET
            = "gaJDlW0vf7en46JwHAOkZsTHvtAiZ3QUd2mD1x26J9w";
    private final String ACCESS_TOKEN
            = "1366513208-MutXEbBMAVOwrbFmZtj1r4Ih2vcoHGHE2207002";
    private final String ACCESS_TOKEN_SECRET
            = "RMPWOePlus3xtURWRVnv1TgrjTyK7Zk33evp4KKyA";

    private Twitter twitter;

    public  TwitterClient(){
        setTwitterInstance();
    }
    public void setTwitterInstance(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        Twitter t = new TwitterFactory(cb.build()).getInstance();

        this.twitter=t;
    }

    public Twitter getTwitterInstance(){
        return this.twitter;
    }

    @Override
        public void publishUuid(TwitterStatusMessage message) throws Exception {
            Status status = this.getTwitterInstance().updateStatus(message.getTwitterPublicationString());
            play.Logger.info("Successfully posted tweet : [" + status.getText() + "].");
        }
}
