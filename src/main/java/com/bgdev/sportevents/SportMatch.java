package com.bgdev.sportevents;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
public class SportMatch extends Event {

    @Getter @Setter
    private String homeTeam;

    @Getter @Setter
    private String awayTeam;

    public SportMatch(LocalDateTime dateTime, String homeTeam, String awayTeam) {
        this.dateTime = dateTime;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    @Override
    public List<String> getParticipant() {
        ArrayList<String> team = new ArrayList<String>();
        team.add(homeTeam);
        team.add(awayTeam);
        return team;
    }
}
