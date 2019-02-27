package com.bgdev.sportevents;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
public abstract class Event {

    @Getter
    @Setter
    LocalDateTime dateTime;

    public abstract List<String> getParticipant();
}
