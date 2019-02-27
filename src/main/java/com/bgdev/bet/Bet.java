package com.bgdev.bet;

import com.bgdev.sportevents.Event;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class Bet {

    @Getter @Setter
    Event event;

    public LocalDateTime getDateTime(){
        return event.getDateTime();
    }
}
