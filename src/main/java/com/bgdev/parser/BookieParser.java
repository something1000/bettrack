package com.bgdev.parser;

import com.bgdev.bet.Bet;

import java.util.List;

public interface BookieParser {
    List<Bet> parse(String url);
}
