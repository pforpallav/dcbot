/*
 * GoogleCalculation.java
 *
 * Copyright (C) 2005 Kokanovic Branko
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.elite.jdcbot.util;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URLEncoder;
import org.json.*;

import org.elite.jdcbot.framework.jDCBot;

/**
 * This class extends WebPageFetcher class and implements Runnable
 * It gets calculation string (e.g. 1+2*3) and returns result using google and WebPageFetcher
 * Reason for implementing Runnable is that it can gets stuck while WebPageFetcher finishes its work
 * Credits for idea goes to http://sourceforge.net/projects/phpdcbot !!!
 * 
 * @since 0.6
 * @author Milos Grbic, Kokanovic Branko
 * @version 0.6
 */
public class GoogleCalculation extends WebPageFetcher implements Runnable {
	
	private jDCBot _bot;
	private String user;
	
	public GoogleCalculation(jDCBot bot, String calc, String u) throws Exception{
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
		
		String url="http://www.google.com/ig/calculator?hl=en&q=";
		_bot=bot;
		user = u;
		calc=URLEncoder.encode(calc,"UTF-8");
		url=url+calc;
		SetURL(url);
	}
	
	private String GetCalculation() throws Exception{

		//retrieving page
		String returned=getPageContent();
		//extracting data from retrived page
		JSONObject j = new JSONObject(returned+"}");
		
		return j.getString("rhs");
		
		/*if (returned.contains(keyword1)==false) throw new Exception();
		String result=returned.substring(returned.indexOf(keyword1)+keyword1.length());
		
		if (result.contains(keyword2)==false) throw new Exception();
		result=result.substring(0,result.indexOf(keyword2));
		if ((result.length()==0) || (result.length()>1000)) throw new Exception();
		
		//replacing html stuff
		
		//replacing decimal dots
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile("<font size=-2> </font>");
		matcher = pattern.matcher(result);
		result = matcher.replaceAll(","); 
		//replacing x (example 2x10^9)
		pattern=Pattern.compile("&#215;");
		matcher=pattern.matcher(result);
		result=matcher.replaceAll("x");
		//replacing sup tags
		pattern=Pattern.compile("<sup>");
		matcher=pattern.matcher(result);
		result=matcher.replaceAll("^");
		pattern=Pattern.compile("</sup>");
		matcher=pattern.matcher(result);
		result=matcher.replaceAll("");
		return result;*/
	}
	
	public void run(){
		
		try{
			String res=this.GetCalculation();
			_bot.SendPublicMessage("Result for user " + user + " : " + res);
		}
		catch (Exception e) {
			try{
				_bot.SendPublicMessage("Result for user " + user + " : " + "Sorry, I can't calculate this:/");
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
}
