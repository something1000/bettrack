package com.bgdev;

import java.time.LocalDate;
import java.time.LocalTime;

public interface BetEvent {
    LocalDate getEventDate();
    LocalTime getEventTime();
    String getHomeTeam();
    String getAwayTeam();
}
