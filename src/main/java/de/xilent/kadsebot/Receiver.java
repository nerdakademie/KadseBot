package de.xilent.kadsebot;

import de.xilent.kadsebot.helper.BotHelper;
import de.xilent.kadsebot.helper.Tracking;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.sound.midi.Track;

/**
 * Created by xilent on 26.06.15.
 */
public class Receiver extends HttpServlet {

    public final static String botURL = "https://api.telegram.org/bot120945382:AAGrADFVuyEIxJJO3KRfdSz7EaoCNtcwwAw";
    public final static String botName = "KadseBot";

    private String message;


    Functions functions;

    public void init() throws ServletException
    {
        // Do required initialization
        message = "Hello World";
        functions = new Functions();
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Actual logic goes here.

        //System.out.print("Test");

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
            if(BotHelper.command(command,"/echo")) {
                functions.echo(jsonObject);
            }else if(BotHelper.command(command,"/engage")){
                functions.engage(jsonObject);
            }else if(BotHelper.command(command,"/debug")){
                functions.debugjson(jsonObject);
            }else if(BotHelper.command(command,"/amazon")){
                functions.searchAmazon(jsonObject);
            }else if (BotHelper.command(command, "/decide")){
                functions.decide(jsonObject);
            }else if(BotHelper.command(command,"/ohkadsewasessenwirheute")){
                functions.ohkadsewasessenwirheute(jsonObject);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }




    public void destroy()
    {
        // do nothing.
    }
}