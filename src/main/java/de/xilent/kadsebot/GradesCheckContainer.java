package de.xilent.kadsebot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GradesCheckContainer {

	private int userID;
	private String cookie;
	private String url = "https://cis.nordakademie.de/pruefungsamt/pruefungsergebnisse/?no_cache=1";
	private Map<String, String> oldGrades;

	public GradesCheckContainer(int userID, String cookie) {
		this.userID = userID;
		this.cookie = cookie;
		try {
			checkLoadOldGrades();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void checkForNewGrades() throws Exception {
		URL obj = new URL(url);
		Map<String, String> newGrades = new HashMap<String, String>();
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");
		con.setRequestProperty("Cookie", "fe_user_typo=" + cookie);
		Map<String, String> grades = getGradesFromInput(con.getInputStream());
		for (String key : grades.keySet()) {
			String newGrade =  grades.get(key);
			if (!oldGrades.containsKey(key)) {
				newGrades.put(key, newGrade);
			}
			String oldGrade = oldGrades.get(key);
			if(!oldGrade.equalsIgnoreCase(newGrade)) {
				newGrades.put(key, newGrade);
			}
		}
		oldGrades = grades;
		if(newGrades.size() > 0) {
			String grade = "";
			for(String s : newGrades.keySet())
				grade += s + ": "+ newGrades.get(s) + "\n";
			Receiver.instance.functions.sendMessage("Du hast neue Noten:\n"+grade, String.valueOf(userID));
		}
	}

	private void checkLoadOldGrades() throws Exception {
		URL obj = new URL(url);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");
		con.setRequestProperty("Cookie", "fe_user_typo=" + cookie);
		oldGrades = getGradesFromInput(con.getInputStream());
	}

	private Map<String, String> getGradesFromInput(InputStream content) throws IOException {
		HashMap<String, String> ret = new HashMap<String, String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		String line = "";
		String tableContent = "";
		boolean read = false;
		while ((line = reader.readLine()) != null) {
			if (line.contains("<tbody/>")) {
				read = false;
			} else if (line.contains("<tbody>")) {
				read = true;
			} else if (read)
				tableContent += line;
		}
		tableContent = tableContent.replace("</tr>", "");
		tableContent = tableContent.replace("</td>", "");
		String[] rows = tableContent.split("<tr>");
		for (String row : rows) {
			String[] fields = row.split("<td>");
			if (fields.length < 6)
				continue;
			ret.put(fields[0], fields[1]);
		}
		return ret;
	}

}
