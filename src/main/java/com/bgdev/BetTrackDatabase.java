package com.bgdev;

import java.sql.*;
import java.time.LocalDateTime;

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

    public void insertMatch(BetEvent event){
        String insertMatch = "{? = CALL InsertMatch(?,?,?)}";
        try {
            CallableStatement statement = connection.prepareCall(insertMatch);
            statement.setString("p_home_team", event.getHomeTeam());
            statement.setString("p_away_team", event.getAwayTeam());
            LocalDateTime lmatchDate = LocalDateTime.of(event.getEventDate(), event.getEventTime());//LocalDateTime.parse(event.getEventDate() + " " + event.getEventTime());

            Timestamp matchDate = Timestamp.valueOf(lmatchDate);
            statement.setTimestamp("p_match_date", matchDate);
            ResultSet rs = statement.executeQuery();
            System.out.println("MECZ ID: "+ rs.getInt(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void persist() {

        BetTrackDatabase btb = BetTrackDatabase.getBetTrackDatabase();


    }




}
