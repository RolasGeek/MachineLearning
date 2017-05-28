package com.studies.SpellingChecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;



public class SpellingCheckClass  {

	
public static String spellCheck(String text) throws IOException {
	String USER_AGENT = "Mozilla/5.0";
	
	String url = "https://api.cognitive.microsoft.com/bing/v5.0/spellcheck/?text=" + text.replace(' ', '+') +"&mode=spell&mkt=en-us";
	
	URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	con.setRequestMethod("GET");
	//add request header
	con.setRequestProperty("Content-Type", "application/json");
	con.setRequestProperty("Ocp-Apim-Subscription-Key", "e5366b95755b43b49a32ea7910462933");
	con.setRequestProperty("User-Agent", USER_AGENT);
	int responseCode = con.getResponseCode();
	System.out.println("GET Response Code :: " + responseCode);
	if (responseCode == HttpURLConnection.HTTP_OK) { // success
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		return response.toString();
	} else {
		return spellCheck(text);
	}
}
}
