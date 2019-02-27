package com.bgdev;

import com.bgdev.bet.Bet;
import com.bgdev.parser.STSParser;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        STSParser sts = new STSParser();

        List<Bet> be = sts.parse("https://www.sts.pl/pl/oferta/zaklady-bukmacherskie/zaklady-sportowe/?action=offer&sport=184&region=6521&league=4080");
        BetTrackDatabase btb = BetTrackDatabase.getBetTrackDatabase();
        for(Bet e : be){
            // System.out.println(e);
            btb.insertMatch(e);
        }
    }
}
