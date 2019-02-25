package com.bgdev;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class STSParser implements BetParser{

    private final String bookieName = "STS";
   // private final String url ="https://www.sts.pl/pl/oferta/zaklady-bukmacherskie/zaklady-sportowe/?action=offer&sport=184&region=6521&league=74367"; //"https://www.sts.pl/pl/oferta/zaklady-bukmacherskie/zaklady-sportowe/?action=offer&sport=184&region=6521&league=4080";
    private static final Pattern dateRegExp = Pattern.compile("([0-9]{2}\\.){2}[0-9]{4}");

    public ArrayList<BetEvent> parse(String url){
        Element bookieOffer;
        try {
            bookieOffer = Jsoup.connect(url).get().getElementById("offerTables");
        } catch (IOException e) {
            System.out.println("Nie udało się pobrać strony");
            return null;
        }

        Elements events = bookieOffer.getElementsByClass("col3");

        ArrayList<BetEvent> eventsList = new ArrayList<>();

        LocalDate date = null;
        LocalDate lastDate = null;
        for(Element e : events){
            date = getEventDate(e);
            if(date == null)
                date = lastDate;

            LocalTime time = getEventTime(e);

            BetEvent1x2 bet = getEventBets(e);
            bet.setEventDate(date);
            bet.setEventTime(time);

            eventsList.add(bet);
            lastDate = date;
        }

        return eventsList;
    }


    public LocalDate getEventDate(Element elementWithDate){
        Elements searchDate = elementWithDate.getElementsByClass("date");
        if(searchDate.isEmpty()){
            return null;
        }
        String stringWithDate = searchDate.get(0).text();
        Matcher extractDate = dateRegExp.matcher(stringWithDate);
        if(!extractDate.find()){
            return null;
        }
        LocalDate date = LocalDate.parse(extractDate.group(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return date;
    }

    public LocalTime getEventTime(Element elementWithTime){
        Elements searchTime = elementWithTime.getElementsByClass("date_time");
        if(searchTime == null) return null;
        String timeString = searchTime.get(0).getElementsByTag("a").text();
        LocalTime time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
        return time;
    }

    public BetEvent1x2 getEventBets(Element elementWithBets){
        Elements searchBets = elementWithBets.getElementsByClass("subTable");
        if(searchBets == null)
            return null;
        searchBets = searchBets.get(0).getElementsByTag("a");

        String team[] = new String[3];
        float odds[] = new float[3];

        int index = 0;
        for(Element e : searchBets){
            //"([0-9]{1,2}\\.[0-9]{2})");
            team[index] = e.text().substring(0,e.text().length()-5);
            odds[index++] = Float.parseFloat(e.getElementsByTag("span").text());
        }
        BetEvent1x2 betEvent = new BetEvent1x2();
        betEvent.setHomeTeam(team[0].trim());
        betEvent.setAwayTeam(team[2].trim());
        betEvent.setHomeOdds(odds[0]);
        betEvent.setDrawOdds(odds[1]);
        betEvent.setAwayOdds(odds[2]);

        return betEvent;
    }


}
