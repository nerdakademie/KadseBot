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
    int ohKadseDate = 0;

    public void init() throws ServletException
    {
        // Do required initialization
        message = "Hello World";
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Actual logic goes here.

        //System.out.print("Test");

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } finally {
            reader.close();
        }

        try {
            JSONObject jsonObject = new JSONObject(sb.toString());

            JSONObject message = jsonObject.getJSONObject("message");
            String command = message.getString("text");
            if(BotHelper.command(command,"/echo")) {
                Functions.echo(jsonObject);
            }else if(BotHelper.command(command,"/engage")){
                Functions.engage(jsonObject);
            }else if(BotHelper.command(command,"/debug")){
                Functions.debugjson(jsonObject);
            }else if(BotHelper.command(command,"/amazon")){
                Functions.searchAmazon(jsonObject);
            }else if (BotHelper.command(command, "/decide")){
                Functions.decide(jsonObject);
            }else if(BotHelper.command(command,"/ohkadsewasessenwirheute")){
                Functions.ohkadsewasessenwirheute(jsonObject,oKadse());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    private boolean oKadse(){
        if(ohKadseDate != new Date().getDay()){
            ohKadseDate = new Date().getDay();
            return true;
        }
        return false;
    }


    public void destroy()
    {
        // do nothing.
    }
}