package de.xilent.kadsebot.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WohlertMi on 29.06.2015.
 */
public class Tracking {

    public static final Map<String, Integer> postCompanies = new HashMap<String, Integer>() {{
        put("global", 0);
        put("ups", 100002);
        put("dhl", 100001);
        put("fedex", 100003);
        put("tnt", 100004);
        put("dpd", 100007);
        put("dpduk", 100010);
        put("oneworld", 100011);
        put("gls", 100005);
        put("sfexpress", 100012);
        put("eshipper", 100008);
        put("toll", 100009);
        put("aramex", 100006);
        put("flyt", 190002);
        put("yunpost", 190008);
        put("bqc", 190011);
        put("xru", 190007);
        put("hhexp", 190003);
        put("yanwen", 190012);
        put("buylogic", 190018);
        put("rush", 190014);
        put("ruston", 190015);
        put("rets", 190017);
        put("007ex", 190016);
    }};

    public static String Base_URL = "http://www.17track.net/api/de/result/post.shtml";
    String params = "?num=JJD000390005584080468&et=0&theme=default&r="+Math.random();

    public static void track(String chat_id, List<String> params){

        String trackingnr="JJD000390005584080468";
        Integer paketService = 0;
        try {

            Document document = Jsoup.connect(Base_URL + "?num=" + trackingnr +"&et="+paketService+"&theme=default&r="+Math.random()).get();
            // selector query
            Elements headerInfo = document.select("div[id=guide]");
            // check results
            for (org.jsoup.nodes.Element each : headerInfo) {

                Elements list = each.select("ul");


                for(org.jsoup.nodes.Element each2 : list){

                    for (int k = 0; k < each.getElementsByTag("li").size(); k++) {
                         String result = each.getElementsByTag("li").get(k).text();
                        System.out.println(result);

                        for (int l = 0; l < each.getElementsByTag("li").size(); l++) {
                            result = each.getElementsByTag("span").get(k).text();
                            System.out.println(result);
                        }
                    }
                }

                // get value
               /* for (int k = 0; k < each.getElementsByTag("p").size(); k++) {
                    result = each.getElementsByTag("p").get(k).text();
                    mainText.add(result);
                }*/
            }
           /* Elements header = document.select("header[class=cluster-header]");
            for (org.jsoup.nodes.Element headerEach : header) {
                result = headerEach.getElementsByTag("h1").text();
                result2 = headerEach.getElementsByTag("p").text();
                article.put("header", result);
                title = result;
                article.put("subheader", result2);

            }
            SpannableString ss1 = new SpannableString(article.get("header"));
            ss1.setSpan(new RelativeSizeSpan(2f), 0, article.get("header").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss1.setSpan(new StyleSpan(Typeface.BOLD), 0, article.get("header").length(), 0);
            SpannableString ss2 = new SpannableString(article.get("subheader"));
            ss2.setSpan(new RelativeSizeSpan(1f), 0, article.get("subheader").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            String mainTextContent = "";
            for (String mainTextElement : mainText) {
                mainTextContent = mainTextContent + mainTextElement + "\n\n";
            }
            contentFormatted = new SpannableStringBuilder(article.get("header") + "\n\n" + article.get("subheader") + "\n\n" + mainTextContent);
            contentFormatted.setSpan(new RelativeSizeSpan(2f), 0, article.get("header").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            contentFormatted.setSpan(new StyleSpan(Typeface.BOLD), 0, article.get("header").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            contentFormatted.setSpan(new RelativeSizeSpan(1.3f), (article.get("header") + "\n\n").length(), (article.get("header") + "\n\n" + article.get("subheader")).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            Elements nextPage = document.select("ol[class=list-pages]");
            Elements siteentrys = nextPage.select("li");

            for (int h = 0; h < siteentrys.size() - 2; h++) {
                Page++;
                mHandler.post(updateProgressbar);
                addPage();
            }*/
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
