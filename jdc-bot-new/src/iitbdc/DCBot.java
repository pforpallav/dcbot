package iitbdc;

import java.io.IOException;

import org.elite.jdcbot.framework.BotException;
import org.elite.jdcbot.framework.jDCBot;
import org.elite.jdcbot.util.FloodMessageThread;
import org.elite.jdcbot.util.GoogleCalculation;
import org.elite.jdcbot.util.TimerThread;

public class DCBot extends jDCBot {
	

	private TimerThread tt;

	public DCBot(String hub_ip, int lp, int ulp) throws IOException {
		//constructs our bot with 100GB share size and 3 slots
		super("DCBot", "10.3.113.29", lp, ulp, "", "www.cse.iitb.ac.in/~pforpallav", "LAN(T1)8", "", "107374182400", 3, 6, false);
		try {
			connect(hub_ip,411); //hub IP
		} catch (BotException e) {
			//logger.error("Exception in ExampleBot()", e);
		} catch (Exception e) {
			//logger.error("Exception in ExampleBot()", e);
		}
	}


	public void onConnect() {
		tt = new FloodMessageThread(this, 1000 * 60 * 10);
		tt.start();
		try {
			//SendPublicMessage("Hi, I'm a simple DCBot. Features will be added soon. :)");
		} catch (Exception e) {}
	}


	public void onPublicMessage(String user, String message) {
		//examine if command starts with +calc (you could put more commands here
		//in 'else if' construction
		if (message.toLowerCase().startsWith("+calc ")) {
			String eq = message.substring(message.indexOf(" ") + 1, message.length());
			try {
				Thread t = new Thread(new GoogleCalculation(this, eq, user));
				t.start();
			} catch (Exception e) {}
		} else
		
		if (message.toLowerCase().startsWith("+r ")) {
			String title = message.substring(message.indexOf(" ") + 1, message.length());
			try {
					Thread t = new Thread(new MovieRating(this, title, user));
					t.start();
				} catch (Exception e) {}
		} else
			
		if (message.toLowerCase().startsWith("+s")) {
			String option;
			String[] attr;
			try {
					if(message.length() == 2){
						Thread t = new Thread(new SuggestMovie(this, "", "", "", user));
						t.start();
					}
					else{
							option = message.substring(message.indexOf(" ") + 1, message.length());
							attr = option.split(" ");
					
							if(attr.length == 1){
								String genre = (attr[0]=="any")?"":attr[0];
								Thread t = new Thread(new SuggestMovie(this, genre, "", "", user));
								t.start();
							}else if(attr.length == 2){
								String genre = (attr[0]=="any")?"":attr[0];
								String rating = (attr[1]=="any")?"":attr[1];
								Thread t = new Thread(new SuggestMovie(this, genre, rating, "", user));
								t.start();
							}else if(attr.length == 3){
								String genre = (attr[0]=="any")?"":attr[0];
								String rating = (attr[1]=="any")?"":attr[1];
								String year = (attr[2]=="any")?"":attr[2];
								Thread t = new Thread(new SuggestMovie(this, genre, rating, year, user));
								t.start();
							}
					}
				} catch (Exception e) {}
		} else
			//if there were no command recognized above, try to get command from database
			if (message.startsWith("+")) { //we show a help message with possible commands
				try {
					SendPublicMessage("Result for user " + user + " : INVALID COMMAND!! Commands available: [1.FindRating] +r <moviename> [2.SuggestMovie] +s <genre> <imdbRating(lowerBound)> <year(rounded-to-nearest-10)> [3.] +calc <arithmetic-function>");
				} catch (Exception e) {}
			}
	}

	/**
	 * Sends user who wants to talk to us a feedback that we're (still) stupid.
	 */
	public void onPrivateMessage(String user, String message) {
		try {
			SendPrivateMessage(user, "Stop stalking me! ;)");
		} catch (Exception e) {}
	}

	public void onDisconnect() {
		super.onDisconnect();
		//logger.info("Disconnected from the hub");
		tt.stopIt();
	}

}