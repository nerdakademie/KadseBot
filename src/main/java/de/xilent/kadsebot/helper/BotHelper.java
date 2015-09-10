package de.xilent.kadsebot.helper;

import de.xilent.kadsebot.Receiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xilent on 28.06.15.
 */
public class BotHelper {

    public static boolean command(String input,String command){
        input = input.toLowerCase();
        return input.contains(command) || input.contains(command + "@" + Receiver.botName) || input.contains(command + " @" + Receiver.botName);
    }
    public static List<String> getParameters(String input,String command){
        ArrayList<String> response = new ArrayList<>();
        if(input.contains(command +" @" + Receiver.botName)){
            String[] split = input.split("\\s+");
            response.addAll(Arrays.asList(split).subList(2, split.length));
        }else{
            String[] split = input.split("\\s+");
            response.addAll(Arrays.asList(split).subList(1, split.length));
        }

        return response;
    }
}
