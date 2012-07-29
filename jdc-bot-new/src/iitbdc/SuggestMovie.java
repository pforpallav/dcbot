package iitbdc;

import org.elite.jdcbot.util.WebPageFetcher;

import java.io.*;
import java.net.*;


import org.elite.jdcbot.framework.jDCBot;

public class SuggestMovie extends WebPageFetcher implements Runnable {
	private jDCBot _bot;
	private String user;
	private String urlParameters;
	
	public SuggestMovie(jDCBot bot, String genre, String rating, String year, String u) throws Exception{
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
		
		urlParameters = "";
		if(genre!=""||rating!=""||year!=""){
			urlParameters = "mood_change=1&";
			if(genre!=""){
				urlParameters += "&mood_category="+genre;
			}
			if(rating!=""){
				urlParameters += "&mood_imdb_rating="+(Integer.parseInt(rating))*10;
			}
			if(year!=""){
				urlParameters += "&mood_year="+year;
			}
		}
		
		user = u;
		_bot = bot;
	}
	
	
	private String GetMovie() throws Exception{
	
		int result=0, fetched=0;
		char[] cbuf=new char[65000];
		String theInfo;
		try{
			
			String request = "http://www.suggestmemovie.com/";
			URL url = new URL(request); 
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();           
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false); 
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("User-Agent", "Mozilla/4.x");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setUseCaches (false);
	
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());

			// due to the nature of sockets, you probably wont get all the text back with one call
			// so we continue to call read until we get -1, which means were done,
			// or until we run out of space in our array of characters
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			while ((fetched!=-1) && (result<65000)) {
				fetched=in.read(cbuf,result,65000-result);
				result+=fetched;
			}
			in.close();
			connection.disconnect();
			
		}catch (UnknownHostException e) {
			System.err.println("Don't know about host: "+e);
		}catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection: "+e);
		}catch (Exception e) {
			System.err.println(e);
		}finally {
			// convert the array of characters to a String
			// being sure to convert only the characters that have
			// data, not the entire 65,000 character array
			theInfo=new String(cbuf,0,result);
		}
		
		FileWriter fstream = new FileWriter("out.html");
		BufferedWriter out = new BufferedWriter(fstream);
		
		out.write(theInfo);
		out.close();
		

		String delimitFront = "<h1>";
		String delimitEnd = "</h1>";
		
		String parsed = theInfo.substring(theInfo.indexOf(delimitFront)+delimitFront.length());
		
		parsed = parsed.substring(0, parsed.indexOf(delimitEnd));
		
		return parsed;
		
		
	}
	
	public void run(){
		
		try{
			String res = this.GetMovie();
			//System.out.println(res);
			//_bot.SendPublicMessage(res);
			_bot.SendPublicMessage("Result for user " + user + " : " + res);
		}
		catch (Exception e) {
			try{
				_bot.SendPublicMessage("Result for user " + user + " : " + "Sorry, I couldn't a movie for your mood :( or maybe Sad Netmon :'(");
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
}
