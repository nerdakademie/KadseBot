package de.xilent.kadsebot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.xilent.kadsebot.helper.BotHelper;

/**
 * Created by xilent on 26.06.15.
 */
public class Receiver extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public final static String botURL = "https://api.telegram.org/bot120945382:AAGrADFVuyEIxJJO3KRfdSz7EaoCNtcwwAw";
	public final static String botName = "KadseBot";

	List<GradesCheckContainer> checkGrades = new ArrayList<GradesCheckContainer>();
	GradesThread checkThread = new GradesThread();
	Functions functions;
	static Receiver instance;
	Map<String, Integer> authCodes = new HashMap<String, Integer>();
	Map<String, File> nameToFile = new HashMap<String, File>();
	List<Integer> registeredGrades = new ArrayList<Integer>();

	public void init() throws ServletException {
		// Do required initialization
		instance = this;
		functions = new Functions(this);
		checkThread.start();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// Actual logic goes here.

		// System.out.print("Test");

		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
		}

		try {
			JSONObject jsonObject = new JSONObject(sb.toString());

			JSONObject message = jsonObject.getJSONObject("message");
			String command = message.getString("text");
			if (BotHelper.command(command, "/echo")) {
				functions.echo(jsonObject);
			} else if (BotHelper.command(command, "/engage")) {
				functions.engage(jsonObject);
			} else if (BotHelper.command(command, "/debug")) {
				functions.debugjson(jsonObject);
			}  else if (BotHelper.command(command, "/decide")) {
				functions.decide(jsonObject);
			} else if (BotHelper.command(command, "/ohkadsewasessenwirheute")) {
				functions.ohkadsewasessenwirheute(jsonObject);
			} else if (BotHelper.command(command, "/ohmagischekadse")) {
				functions.ohmagischekadse(jsonObject);
			} else if (BotHelper.command(command, "/otherchat")) {
				functions.otherChat(jsonObject);
			} else if ((BotHelper.command(command, "/help")) || (BotHelper.command(command, "/?"))) {
				functions.help(jsonObject);
			} else if ((BotHelper.command(command, "/grades"))) {
				functions.grades(jsonObject);
			} else if ((BotHelper.command(command, "/catme"))){
				functions.catme(jsonObject);
			}
			else {
				functions.unknown(jsonObject);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameterMap().keySet().contains("genCode")) {
			String authKey = Functions.nextAuthKey();
			authCodes.put(authKey, Integer.parseInt(request.getParameter("genCode")));
			response.getWriter().println(authKey);
			return;
		}
		String authcode = request.getParameter("authcode");
		String user = request.getParameter("benutzername");
		String pass = request.getParameter("passwort");
		if (authCodes.containsKey(authcode)) {
			int userId = authCodes.get(authcode);
			authCodes.remove(authcode);
			GradesCheckContainer t = new GradesCheckContainer(userId, user, pass);
			registeredGrades.add(userId);
			checkGrades.add(t);
			functions.sendMessage("KadseBot schnuppert jetzt hin und wieder mal in deinen Noten rum.", String.valueOf(userId));
			request.getRequestDispatcher("/WEB-INF/success.jsp").forward(request, response);
			response.setContentType("text/html");
			return;
		}
		request.getRequestDispatcher("/WEB-INF/index.jsp").include(request, response);
		response.setContentType("text/html");
	}

	public void destroy() {
		checkThread.interrupt();
		for(GradesCheckContainer c : this.checkGrades) {
			functions.sendMessage("Kadse m�de, Kadse schlafen.", String.valueOf(c.userID));
		}
			
	}
}