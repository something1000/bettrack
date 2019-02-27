package com.bgdev.parser;

import com.bgdev.bet.Bet;
import com.bgdev.bet.Bet1X2;
import com.bgdev.sportevents.SportMatch;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class STSParser implements BookieParser {

    private final String bookieName = "STS";
   // private final String url ="https://www.sts.pl/pl/oferta/zaklady-bukmacherskie/zaklady-sportowe/?action=offer&sport=184&region=6521&league=74367"; //"https://www.sts.pl/pl/oferta/zaklady-bukmacherskie/zaklady-sportowe/?action=offer&sport=184&region=6521&league=4080";
    private static final Pattern dateRegExp = Pattern.compile("([0-9]{2}\\.){2}[0-9]{4}");

    public ArrayList<Bet> parse(String url){
        Element bookieOffer;
        try {
            bookieOffer = Jsoup.connect(url).get().getElementById("offerTables");
        } catch (IOException e) {
            System.out.println("Nie udało się pobrać strony");
            return null;
        }

        Elements events = bookieOffer.getElementsByClass("col3");

        ArrayList<Bet> eventsList = new ArrayList<>();

        LocalDate date = null;
        LocalDate lastDate = null;
        for(Element e : events){
            date = getEventDate(e);
            if(date == null)
                date = lastDate;

            LocalTime time = getEventTime(e);
            LocalDateTime datetime = LocalDateTime.of(date, time);

            Bet1X2 bet = getEventBets(e, datetime);

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

    public Bet1X2 getEventBets(Element elementWithBets, LocalDateTime datetime){
        Elements searchBets = elementWithBets.getElementsByClass("subTable");
        if(searchBets == null)
            return null;
        searchBets = searchBets.get(0).getElementsByTag("a");

        String team[] = new String[3];
        float odds[] = new float[3];

        int index = 0;
        for(Element e : searchBets){
            team[index] = e.text().substring(0,e.text().length()-5);
            odds[index++] = Float.parseFloat(e.getElementsByTag("span").text());
        }
        SportMatch sportMatch = new SportMatch(datetime, team[0], team[2]);
        Bet1X2 betEvent = new Bet1X2(sportMatch, odds[0],odds[1],odds[2]);

        return betEvent;
    }


}
