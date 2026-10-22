package src;

import java.util.ArrayList;
import java.util.Collections;

public class Bag {
    private ArrayList<Character> letters;

    public Bag() {
        initBag();
    }
    public ArrayList<Character> getLetters() {
        return letters;
    }
    private void initBag() {
        String LETTER_DISTR = "";
        switch (Scrabble.LANGUAGE.toLowerCase()) {
            case "english":
                LETTER_DISTR = "9:A|2:B|2:C|4:D|12:E|2:F|3:G|2:H|9:I|1:J|1:K|4:L|2:M|6:N|8:O|2:P|1:Q|6:R|4:S|6:T|4:U|2:V|2:W|1:X|2:Y|1:Z|2:%|";
                break;
            case "german":
                LETTER_DISTR = "5:A|2:B|2:C|4:D|15:E|2:F|3:G|4:H|6:I|1:J|2:K|3:L|4:M|9:N|3:O|1:P|1:Q|6:R|7:S|6:T|6:U|1:V|1:W|1:X|1:Y|1:Z|2:%|1:Ä|1:Ü|1:Ö|";
                break;
            case "romanian":
                LETTER_DISTR = "12:A|1:B|2:C|3:D|11:E|1:F|1:G|1:H|8:I|1:J|2:L|2:M|6:N|5:O|2:P|7:R|7:S|7:T|5:U|1:V|1:X|1:Z|2:Ă|1:Â|1:Î|2:Ș|2:Ț|2:%|";
                break;
            default: // default to english tile set
                LETTER_DISTR = "9:A|2:B|2:C|4:D|12:E|2:F|3:G|2:H|9:I|1:J|1:K|4:L|2:M|6:N|8:O|2:P|1:Q|6:R|4:S|6:T|4:U|2:V|2:W|1:X|2:Y|1:Z|2:%|";
        }
        letters = new ArrayList <Character>();
        String[] temp = LETTER_DISTR.split("\\|");
        for(String letterDistr: temp) {
            int letterNum = Integer.parseInt(letterDistr.substring(0,letterDistr.indexOf(":")));
            char letter = letterDistr.charAt(letterDistr.indexOf(":") + 1);
            for(int i = 1; i <= letterNum; i++) {
                letters.add(letter);
            }
        }
        Collections.shuffle(letters);
    }
    public int size() {
        return letters.size();
    }
    public Character remove() {
        if(size() == 0) return '-';
        else return letters.remove(0);
    }
    public void add(Character letter) {
        letters.add(letter);
        Collections.shuffle(letters);
    }
}
