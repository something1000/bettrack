package com.bgdev;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@ToString
public class BetEvent1x2 implements BetEvent{

    public BetEvent1x2(LocalDate eventDate, LocalTime eventTime, String homeTeam, float homeOdds, float drawOdds, String awayTeam, float awayOdds) {
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.homeTeam = homeTeam;
        this.homeOdds = homeOdds;
        this.drawOdds = drawOdds;
        this.awayTeam = awayTeam;
        this.awayOdds = awayOdds;
    }

    public BetEvent1x2(){};

    @Getter @Setter
    private LocalDate eventDate;

    @Getter @Setter
    private LocalTime eventTime;

    @Getter @Setter
    private String homeTeam;
    @Getter @Setter
    private float homeOdds;

    @Getter
    @Setter
    private float drawOdds;

    @Getter @Setter
    private String awayTeam;

    @Getter @Setter
    private float awayOdds;


}
