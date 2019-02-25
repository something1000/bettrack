package com.bgdev;

import java.util.List;

public interface BetParser {
    List<BetEvent> parse(String url);
}
