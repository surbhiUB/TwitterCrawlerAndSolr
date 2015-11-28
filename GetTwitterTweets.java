package twitter;
import twitter4j.conf.*;
import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.api.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class GetTwitterTweets {
	Twitter twitter;
	String searchString = "IPhone AND lang:en";
	List<Status> tweets;
	int totalTweets;

	void setup()
	{


		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setJSONStoreEnabled(true);
		cb.setOAuthConsumerKey("cPUWJd4mPzCjMMOvPbyUnaIGy");
		cb.setOAuthConsumerSecret("6lbOa0G3vBzWRayKaEyUPbVdp3n4jTr0uKMfTEiOsWT31FP1Ik");
		cb.setOAuthAccessToken("339570255-ldVm4YW8yTijzi79myGOaSQQKbm6LXUpFon1xWUm");
		cb.setOAuthAccessTokenSecret("FNjh6SL37GbzZHkCR2Yhryp2Q3YbzJVRkJh1UVc9AjeYC");

		TwitterFactory tf = new TwitterFactory(cb.build());

		twitter = tf.getInstance();
		getNewTweets();
	}


	void getNewTweets(){
		try{
			Query query = new Query(searchString);

			query.setCount(50); //by default its 15
			QueryResult result = twitter.search(query);
			tweets = result.getTweets(); // the return of this function is list
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			PrintWriter writer = new PrintWriter("final_tweet_"+timeStamp+".json", "UTF-8");


			for (Status tweet : tweets) {
				//System.out.println(tweet.getFromUser() + ":" + tweet.getText());
				String json = TwitterObjectFactory.getRawJSON(tweet);
				System.out.println(json);

				JSONObject obj = new JSONObject(json);

				JSONObject newObj = new JSONObject();

				//System.out.println(obj.getString("text"));

				newObj.put("id",obj.getString("id"));
				newObj.put("text",obj.getString("text"));
				newObj.put("lang",obj.getString("lang"));
				newObj.put("created_at", obj.getString("created_at"));


				JSONObject data = obj.getJSONObject("entities");
				JSONArray htArray = data.getJSONArray("hashtags");
				JSONArray urlArray = data.getJSONArray("urls");

				int i;
				JSONArray extArray=new JSONArray();
				for(i=0;i<urlArray.length();i++)
				{
					JSONObject jsonUrlObj = urlArray.getJSONObject(i);
					extArray.put(jsonUrlObj.get("expanded_url"));

				}
				newObj.put("tweet_url", extArray);
				JSONArray hashT=new JSONArray();
				for(i=0;i<htArray.length();i++)
				{
					JSONObject jsonHtObj = htArray.getJSONObject(i);
					hashT.put(jsonHtObj.get("text"));

				}
				newObj.put("tweet_hashtags", hashT);
				


		//newObj.put("twitter_hashtags",obj.getString("twitter_hashtags"));
		//newObj.put("twitter_urls",obj.getString("twitter_urls"));

		writer.println(newObj);
	}
	writer.close();

	/*tweets = result.getTweets();

            for(Status s:tweets){
                totalTweets++;
                System.out.println(tweets.toString());
            }

            System.out.println("totalTweets "+totalTweets);
	 */
	//printTweetsToFile();
}
catch(Exception e){
	System.out.println("Exception "+e);
}


}


void printTweetsToFile(){
	//System.out.println(tweets);
	try {
		PrintWriter writer = new PrintWriter("text123.json", "UTF-8");
		writer.println(tweets);
		//writer.println("The second line"+tweets.toString());
		writer.close();
	}
	catch(Exception e){
		System.out.println("Exception e");
	}
}
public static void main(String[] args) {

	GetTwitterTweets GetTwitterTweetsObj = new GetTwitterTweets();
	GetTwitterTweetsObj.setup();
}

}
