package com.bgdev;

import com.bgdev.bet.Bet;
import com.bgdev.sportevents.Event;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class BetTrackDatabase {

    private static BetTrackDatabase instance = null;
    private Connection connection;
    private static final String url= "jdbc:mysql://localhost:3306/bettrack?useSSL=false";
    private static final String username= "admin";
    private static final String password= "1234";

    private BetTrackDatabase(){
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("BetTrackDatabase.class: can't connect to database");
            e.printStackTrace();
        }
    }


    public static BetTrackDatabase getBetTrackDatabase(){
            if(instance == null){
                instance = new BetTrackDatabase();
            }
            return instance;
    }

    public void disconnectDatabase(){
        if(instance == null) return;
        try {
            connection.close();
            connection = null;
            instance = null;
        } catch (SQLException e) {
            System.out.println("BetTrackDatabase.class: can't disconnect database");
        }
    }

    public void insertMatch(Bet bet){
        String insertMatch = "{? = CALL InsertMatch(?,?,?)}";
        try {
            CallableStatement statement = connection.prepareCall(insertMatch);
            List<String> teams = bet.getEvent().getParticipant();
            statement.setString("p_home_team", teams.get(0));
            statement.setString("p_away_team", teams.get(1));

           // LocalDateTime lmatchDate = LocalDateTime.of(bet.getEventDate(), bet.getEventTime());//LocalDateTime.parse(event.getEventDate() + " " + event.getEventTime());
            Timestamp matchDate = Timestamp.valueOf(bet.getDateTime());

            statement.setTimestamp("p_match_date", matchDate);
            ResultSet rs = statement.executeQuery();
            rs.next();
            System.out.println("MECZ ID: "+ rs.getInt(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void persist() {

        BetTrackDatabase btb = BetTrackDatabase.getBetTrackDatabase();


    }




}
