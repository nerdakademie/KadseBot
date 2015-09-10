package de.xilent.kadsebot;

import de.xilent.kadsebot.helper.BotHelper;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by xilent on 28.06.15.
 */
public class Functions {
    public static String charset = "UTF-8";

    public static void echo( JSONObject JSONInput){
        new Thread(() -> {
            try {
                JSONObject message = JSONInput.getJSONObject("message");
                JSONObject chat = message.getJSONObject("chat");
                System.out.println(chat.get("id"));

                String text = message.getString("text");
                List<String> params = BotHelper.getParameters(text, "/echo");

                if(params.size() >0) {

                    StringBuilder builder = new StringBuilder();
                    for(String s : params) {
                        builder.append(s);
                    }

                    String query = String.format("/sendMessage?chat_id=%s&text=%s",
                            URLEncoder.encode(String.valueOf(chat.getInt("id")), charset),
                            URLEncoder.encode( builder.toString(), charset));
                    System.out.println(query);

                    URL obj = new URL(Receiver.botURL + query);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // optional default is GET
                    con.setRequestMethod("GET");

                    con.getResponseCode();
                }else{
                    sendUsageMessage("/echo","/echo yourtextgoeshere",String.valueOf(chat.getInt("id")));
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();

    }

    public static void debugjson( JSONObject JSONInput){
        new Thread(() -> {
            try {
                JSONObject message = JSONInput.getJSONObject("message");
                JSONObject chat = message.getJSONObject("chat");

                String text = message.getString("text");

                String query = String.format("/sendMessage?chat_id=%s&text=%s",
                        URLEncoder.encode(String.valueOf(chat.getInt("id")), charset),
                        URLEncoder.encode(JSONInput.toString(), charset));

                URL obj = new URL(Receiver.botURL + query);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // optional default is GET
                con.setRequestMethod("GET");
                con.getResponseCode();
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();

    }

    public static void searchAmazon(JSONObject JSONInput){
        String url = "http://www.amazon.de/s/field-keywords=";
        String output= "";


        JSONObject message = JSONInput.getJSONObject("message");
        JSONObject chat = message.getJSONObject("chat");

        String text = message.getString("text");
        List<String> params = BotHelper.getParameters(text, "/amazon");


        try {
            if(params.size() >0) {

                StringBuilder builder = new StringBuilder();
                for (String s : params) {
                    builder.append(s +" ");
                }
                Document document = Jsoup.connect(url + builder.toString()).get();
                Elements headerInfo = document.select("ul[id=s-results-list-atf]");
                // check results
                for (org.jsoup.nodes.Element each : headerInfo) {

                    Elements list = each.select("li");

                    for (Element eachli : list) {
                        Elements link = eachli.select("a[class=a-link-normal s-access-detail-page  a-text-normal]");
                        if(link.attr("title").length()>2) {
                            //output = output + link.attr("title") + "  :  " + link.attr("href") + "\n";
                            Document document2 = Jsoup.connect(link.attr("href")).get();
                            Elements tradein = document2.select("span[id=tradeInButton_tradeInValueLine]");
                            if (tradein.size() >0){
                                Elements spanPrice = tradein.select("span[class=a-text-bold]");
                                System.out.println(spanPrice.get(0).html());
                                output = output + link.attr("title") + " : " +spanPrice.get(0).html() + " | " + link.attr("href") +"\n";
                            }


                        }
                    }
                }

                if(output.length() <5){
                    output = "No search results for: " +builder.toString();
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

        }catch (IOException e){
            e.printStackTrace();
        }


    }

    public static void engage( JSONObject JSONInput){
        new Thread(() -> {
            try {
                JSONObject message = JSONInput.getJSONObject("message");
                JSONObject chat = message.getJSONObject("chat");
                System.out.println(chat.get("id"));
                String text = message.getString("text");
                List<String> params = BotHelper.getParameters(text, "/engage");

                if(params.size() ==1) {
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
                }else if(params.size()>1){
                    sendErrorMessage("You must enter one parameter. Not less not more",String.valueOf(chat.getInt("id")));
                }else{
                    sendUsageMessage("/engage","/engage @UserName",String.valueOf(chat.getInt("id")));
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();

    }

    public static void decide(JSONObject JSONInput){

        new Thread(() -> {
            try {
                JSONObject message = JSONInput.getJSONObject("message");
                JSONObject chat = message.getJSONObject("chat");
                System.out.println(chat.get("id"));

                String text = message.getString("text");
                List<String> params = BotHelper.getParameters(text, "/decide");

                if(params.size() >0) {

                    String result = params.get(generateRandomNumber(0, params.size() - 1));

                    String response = "Ergebnis: " + result;


                    String query = String.format("/sendMessage?chat_id=%s&text=%s",
                            URLEncoder.encode(String.valueOf(chat.getInt("id")), charset),
                            URLEncoder.encode( response, charset));
                    System.out.println(query);

                    URL obj = new URL(Receiver.botURL + query);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // optional default is GET
                    con.setRequestMethod("GET");

                    con.getResponseCode();
                }else{
                    sendUsageMessage("/decide","/decide option1 option2 ...",String.valueOf(chat.getInt("id")));
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();
    }

    public static void ohkadsewasessenwirheute(JSONObject JSONInput){
        JSONInput.getJSONObject("message").remove("text");
        JSONInput.getJSONObject("message").put("text","/decide Penny Smileys Mensa");
        decide(JSONInput);

    }

    public static void sendErrorMessage(String ErrorMessage,String chatID){
        try {
            String query = String.format("/sendMessage?chat_id=%s&text=%s",
                    URLEncoder.encode(chatID, charset),
                    URLEncoder.encode("Error: " +ErrorMessage, charset));


            URL obj = new URL(Receiver.botURL + query);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");


            con.getResponseCode();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void sendUsageMessage(String command,String UsageMessage,String chatID){
        try {
            String query = String.format("/sendMessage?chat_id=%s&text=%s",
                    URLEncoder.encode(chatID, charset),
                    URLEncoder.encode(command + " Usage: " +UsageMessage, charset));


            URL obj = new URL(Receiver.botURL + query);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");


            con.getResponseCode();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static int generateRandomNumber(int min, int max){

        return new Random().nextInt((max - min) + 1) + min;

    }


}
