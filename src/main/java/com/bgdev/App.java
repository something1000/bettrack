package com.bgdev;

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

        List<BetEvent> be = sts.parse("https://www.sts.pl/pl/oferta/zaklady-bukmacherskie/zaklady-sportowe/?action=offer&sport=184&region=6521&league=4080");
        for(BetEvent e : be){
            System.out.println(e);
        }
    }
}
