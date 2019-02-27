package com.bgdev.bet;

import com.bgdev.sportevents.Event;
import com.bgdev.sportevents.SportMatch;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
public class Bet1X2 extends Bet {



    public Bet1X2(SportMatch match, float homeOdds, float drawOdds, float awayOdds) {
        this.event = match;
        this.homeOdds = homeOdds;
        this.drawOdds = drawOdds;
        this.awayOdds = awayOdds;
    }

    public Bet1X2(){};


    @Getter @Setter
    private float homeOdds;


    @Getter @Setter
    private float drawOdds;

    @Getter @Setter
    private float awayOdds;


}
