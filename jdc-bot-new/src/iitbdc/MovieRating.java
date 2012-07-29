package iitbdc;

import org.elite.jdcbot.util.WebPageFetcher;
import java.net.*;

import org.json.*;

import org.elite.jdcbot.framework.jDCBot;

public class MovieRating extends WebPageFetcher implements Runnable {
	private jDCBot _bot;
	private String user;
	
	public MovieRating(jDCBot bot, String t, String u) throws Exception{
		final String authUser = "pforpallav";
		final String authPassword = "buckmin$ter";
		Authenticator.setDefault(
		   new Authenticator() {
		      @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                //logger.info(MessageFormat.format("Generating PasswordAuthentitcation for proxy authentication, using username={0} and password={1}.", username, password));
	                return new PasswordAuthentication(authUser, authPassword.toCharArray());
	            }
		   }
		);

		System.setProperty("http.proxyHost", "netmon.iitb.ac.in");
		System.setProperty("http.proxyPort", "80");
		System.setProperty("http.proxyUser", authUser);
		System.setProperty("http.proxyPassword", authPassword);
		
		String url="http://www.imdbapi.com/?t=";
		_bot=bot;
		user = u;
		t=URLEncoder.encode(t,"UTF-8");
		url=url+t+"&tomatoes=true";
		SetURL(url);
	}
	
	private String[] GetRating() throws Exception{

		//retrieving page
		String returned=getPageContent();
		//extracting data from retrived page
		JSONObject j = new JSONObject(returned+"}");
		
		String result[] = new String[4];
		result[0] = j.getString("Title");
		result[1] = j.getString("Genre");
		result[2] = j.getString("imdbRating");
		result[3] = j.getString("tomatoMeter");
		return result;
		
	}
	
	public void run(){
		
		try{
			String[] res = this.GetRating();
			_bot.SendPublicMessage("Result for user " + user + " : \n" + res[0] + " \n" + "Genre: " + res[1] + " \nIMDB Rating: " + res[2] + "/10 \nTomatoMETER: " + res[3] + "/100");
		}
		catch (Exception e) {
			try{
				_bot.SendPublicMessage("Result for user " + user + " : " + "Sorry, I couldn't find this movie :( Sad Netmon :(");
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
}
