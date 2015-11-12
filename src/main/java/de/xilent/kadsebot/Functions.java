package de.xilent.kadsebot;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.xilent.kadsebot.helper.BotHelper;

/**
 * Created by xilent on 28.06.15.
 */
public class Functions {
	public static String charset = "UTF-8";

	HashMap<Integer, int[]> ohKadseChatCounter;
	private final static SecureRandom random = new SecureRandom();
	protected static Receiver receiver;

	public Functions(Receiver rec) {
		receiver = rec;
	}

	public void echo(JSONObject JSONInput) {
		new Thread(() -> {
			try {
				JSONObject message = JSONInput.getJSONObject("message");
				JSONObject chat = message.getJSONObject("chat");
				System.out.println(chat.get("id"));

				String text = message.getString("text");
				List<String> params = BotHelper.getParameters(text, "/echo");

				if (params.size() > 0) {

					StringBuilder builder = new StringBuilder();
					for (String s : params) {
						builder.append(s);
						builder.append(" ");
					}

					String query = String.format("/sendMessage?chat_id=%s&text=%s",
							URLEncoder.encode(String.valueOf(chat.getInt("id")), charset),
							URLEncoder.encode(builder.toString(), charset));
					System.out.println(query);

					URL obj = new URL(Receiver.botURL + query);
					HttpURLConnection con = (HttpURLConnection) obj.openConnection();

					// optional default is GET
					con.setRequestMethod("GET");

					con.getResponseCode();
				} else {
					sendUsageMessage("/echo", "/echo yourtextgoeshere", String.valueOf(chat.getInt("id")));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

	}

	public void debugjson(JSONObject JSONInput) {
		new Thread(() -> {
			try {
				JSONObject message = JSONInput.getJSONObject("message");
				JSONObject chat = message.getJSONObject("chat");

				// String text = message.getString("text");

				String query = String.format("/sendMessage?chat_id=%s&text=%s",
						URLEncoder.encode(String.valueOf(chat.getInt("id")), charset),
						URLEncoder.encode(JSONInput.toString(), charset));

				URL obj = new URL(Receiver.botURL + query);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				// optional default is GET
				con.setRequestMethod("GET");
				con.getResponseCode();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

	}

	public void searchAmazon(JSONObject JSONInput) {
		String url = "http://www.amazon.de/s/field-keywords=";
		String output = "";

		JSONObject message = JSONInput.getJSONObject("message");
		JSONObject chat = message.getJSONObject("chat");

		String text = message.getString("text");
		List<String> params = BotHelper.getParameters(text, "/amazon");

		try {
			if (params.size() > 0) {

				StringBuilder builder = new StringBuilder();
				for (String s : params) {
					builder.append(s + " ");
				}
				Document document = Jsoup.connect(url + builder.toString()).get();
				Elements headerInfo = document.select("ul[id=s-results-list-atf]");
				// check results
				for (org.jsoup.nodes.Element each : headerInfo) {

					Elements list = each.select("li");

					for (Element eachli : list) {
						Elements link = eachli.select("a[class=a-link-normal s-access-detail-page  a-text-normal]");
						if (link.attr("title").length() > 2) {
							// output = output + link.attr("title") + " : " +
							// link.attr("href") + "\n";
							Document document2 = Jsoup.connect(link.attr("href")).get();
							Elements tradein = document2.select("span[id=tradeInButton_tradeInValueLine]");
							if (tradein.size() > 0) {
								Elements spanPrice = tradein.select("span[class=a-text-bold]");
								System.out.println(spanPrice.get(0).html());
								output = output + link.attr("title") + " : " + spanPrice.get(0).html() + " | "
										+ link.attr("href") + "\n";
							}

						}
					}
				}

				if (output.length() < 5) {
					output = "No search results for: " + builder.toString();
				}
				String query = String.format("/sendMessage?chat_id=%s&text=%s",
						URLEncoder.encode(String.valueOf(chat.getInt("id")), charset),
						URLEncoder.encode(output, charset));

				URL obj = new URL(Receiver.botURL + query);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				// optional default is GET
				con.setRequestMethod("GET");
				con.getResponseCode();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void grades(JSONObject JSONInput) {
		new Thread(() -> {
			try {
				JSONObject message = JSONInput.getJSONObject("message");
				JSONObject user = message.getJSONObject("from");
				int id = user.getInt("id");
				String authKey = nextAuthKey();
				if (receiver.authCodes.containsKey(id))
					receiver.authCodes.remove((Integer) id);
				receiver.authCodes.put(authKey, id);

				String query = String.format("/sendMessage?chat_id=%s&text=%s",
						URLEncoder.encode(String.valueOf(id), charset),
						URLEncoder.encode("Bitte autorisiere dich auf:\n" + "https://bot.michel-wohlert.de/bot/\n"
								+ "Mit dem Autorisierungscode:" + authKey, charset));

				URL obj = new URL(Receiver.botURL + query);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				// optional default is GET
				con.setRequestMethod("GET");
				con.getResponseCode();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

	}

	public void engage(JSONObject JSONInput) {
		new Thread(() -> {
			try {
				JSONObject message = JSONInput.getJSONObject("message");
				JSONObject chat = message.getJSONObject("chat");
				System.out.println(chat.get("id"));
				String text = message.getString("text");
				List<String> params = BotHelper.getParameters(text, "/engage");

				if (params.size() == 1) {
					String query = String.format("/sendMessage?chat_id=%s&text=%s",
							URLEncoder.encode(String.valueOf(chat.getInt("id")), charset),
							URLEncoder.encode("Meow " + params.get(0), charset));

					URL obj = new URL(Receiver.botURL + query);

					for (int i = 0; i < 5; i++) {
						HttpURLConnection con = (HttpURLConnection) obj.openConnection();

						// optional default is GET
						con.setRequestMethod("GET");
						con.getResponseCode();
					}
				} else if (params.size() > 1) {
					sendErrorMessage("You must enter one parameter. Not less not more",
							String.valueOf(chat.getInt("id")));
				} else {
					sendUsageMessage("/engage", "/engage @UserName", String.valueOf(chat.getInt("id")));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

	}

	public void decide(JSONObject JSONInput) {

		new Thread(() -> {
			try {
				JSONObject message = JSONInput.getJSONObject("message");

				String text = message.getString("text");
				List<String> params = BotHelper.getParameters(text, "/decide");

				if (params.size() > 0) {

					String response = "Ergebnis: " + params.get(generateRandomNumber(0, params.size() - 1));

					String query = String.format("/sendMessage?chat_id=%s&text=%s",
							URLEncoder.encode(String.valueOf(message.getJSONObject("chat").getInt("id")), charset),
							URLEncoder.encode(response, charset));
					System.out.println(query);

					URL obj = new URL(Receiver.botURL + query);
					HttpURLConnection con = (HttpURLConnection) obj.openConnection();

					// optional default is GET
					con.setRequestMethod("GET");

					con.getResponseCode();
				} else {
					sendUsageMessage("/decide", "/decide option1 option2 ...",
							String.valueOf(message.getJSONObject("chat").getInt("id")));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void ohkadsewasessenwirheute(JSONObject JSONInput) {
		int chat_id = JSONInput.getJSONObject("message").getJSONObject("chat").getInt("id");
		int ohKadseResponse = oKadse(chat_id);
		if (ohKadseResponse == 0) {
			JSONInput.getJSONObject("message").remove("text");
			JSONInput.getJSONObject("message").put("text", "/decide Penny Smileys Mensa");
			decide(JSONInput);
		} else if (ohKadseResponse > 0 && ohKadseResponse < 3) {
			sendMessage("Kadse müde, Kadse schlafen", String.valueOf(chat_id));
		} else {
			String username = "";
			try {
				username = JSONInput.getJSONObject("message").getJSONObject("from").getString("username");

			} catch (Exception e) {
				e.printStackTrace();
			}
			if (username.length() > 1) {
				sendMessage("Ob du behindert bist @" + username + " . Kadse hat entschieden",
						String.valueOf(JSONInput.getJSONObject("message").getJSONObject("chat").getInt("id")));
			} else {
				sendMessage("Ob du behindert bist. Kadse hat entschieden",
						String.valueOf(JSONInput.getJSONObject("message").getJSONObject("chat").getInt("id")));
			}
		}

	}

	public void ohmagischekadse(JSONObject JSONInput) {
		JSONInput.getJSONObject("message").remove("text");
		JSONInput.getJSONObject("message").put("text", "/decide Ja Nein Vielleicht Frag-Später");
		decide(JSONInput);
	}

	public void otherChat(JSONObject JSONInput) {
		new Thread(() -> {
			try {
				JSONObject message = JSONInput.getJSONObject("message");
				JSONObject chat = message.getJSONObject("chat");
				System.out.println(chat.get("id"));
				String text = message.getString("text");
				List<String> params = BotHelper.getParameters(text, "/otherchat");
				if (params.size() > 1 && isInteger(params.get(0))) {

					StringBuilder builder = new StringBuilder();
					for (String s : params.subList(1, params.size())) {
						builder.append(s);
						builder.append(" ");
					}

					String query = String.format("/sendMessage?chat_id=%s&text=%s",
							URLEncoder.encode(params.get(0), charset), URLEncoder.encode(builder.toString(), charset));

					URL obj = new URL(Receiver.botURL + query);

					HttpURLConnection con = (HttpURLConnection) obj.openConnection();

					// optional default is GET
					con.setRequestMethod("GET");
					con.getResponseCode();

				} else if (params.size() > 1) {
					sendErrorMessage(
							"You must enter at least 2 parameters. The first one has to be a chatID. Please dont abuse",
							String.valueOf(chat.getInt("id")));
				} else {
					sendUsageMessage("/otherchat", "/otherchat chatID Your Message here",
							String.valueOf(chat.getInt("id")));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void help(JSONObject jsonObject) {
		sendMessage("Possible commands: \n /echo \n /debug \n /ohkadsewasessenwirheute \n /ohmagischekadse \n /decide",
				String.valueOf(jsonObject.getJSONObject("message").getJSONObject("chat").getInt("id")));
	}

	public void unknown(JSONObject jsonObject) {
		sendMessage("Kadse verwirrt, kadse kennt nicht.",
				String.valueOf(jsonObject.getJSONObject("message").getJSONObject("chat").getInt("id")));
	}

	public void sendMessage(String Message, String chatID) {
		try {
			String query = String.format("/sendMessage?chat_id=%s&text=%s", URLEncoder.encode(chatID, charset),
					URLEncoder.encode(Message, charset));

			URL obj = new URL(Receiver.botURL + query);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			con.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendErrorMessage(String ErrorMessage, String chatID) {
		sendMessage("Error: " + ErrorMessage, chatID);

	}

	public void sendUsageMessage(String command, String UsageMessage, String chatID) {
		sendMessage(command + " Usage: " + UsageMessage, chatID);
	}

	private int generateRandomNumber(int min, int max) {

		return new Random().nextInt((max - min) + 1) + min;

	}

	private int oKadse(int ChatID) {
		if (ohKadseChatCounter == null) {
			ohKadseChatCounter = new HashMap<>();
		}
		if (!ohKadseChatCounter.containsKey(ChatID)) {
			ohKadseChatCounter.put(ChatID, new int[] { new GregorianCalendar().get(Calendar.DAY_OF_MONTH), 0 });
			return ohKadseChatCounter.get(ChatID)[1];
		} else {
			if (ohKadseChatCounter.get(ChatID)[0] != new GregorianCalendar().get(Calendar.DAY_OF_MONTH)) {
				ohKadseChatCounter.put(ChatID, new int[] { new GregorianCalendar().get(Calendar.DAY_OF_MONTH), 0 });
				return ohKadseChatCounter.get(ChatID)[1];
			} else {
				ohKadseChatCounter.put(ChatID,
						new int[] { ohKadseChatCounter.get(ChatID)[0], ohKadseChatCounter.get(ChatID)[1] + 1 });
				return ohKadseChatCounter.get(ChatID)[1];
			}
		}

	}

	public static boolean isInteger(String s) {
		return isInteger(s, 10);
	}

	public static boolean isInteger(String s, int radix) {
		if (s.isEmpty())
			return false;
		for (int i = 0; i < s.length(); i++) {
			if (i == 0 && s.charAt(i) == '-') {
				if (s.length() == 1)
					return false;
				else
					continue;
			}
			if (Character.digit(s.charAt(i), radix) < 0)
				return false;
		}
		return true;
	}

	public static String nextAuthKey() {
		return String.valueOf(random.nextInt((9999) + 1));
	}

}
