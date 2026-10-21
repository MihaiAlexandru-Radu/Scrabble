package Scrabble;

import java.util.HashMap;
import java.util.Map;

public class Scrabble {
    public static final String LANGUAGE = "english";

    private static final Map<Character, Integer> LETTER_VALUES;

    private static Board board;
    private static Bag bag;
    private static Player p1;
    private static Player p2;
    private static Frame frame; 
    private static Solver solver;

    public static void main(String[] args) throws InterruptedException {
        board = new Board();
        bag = new Bag();
        p1 = new Player();
        p2 = new Player();
        frame = new Frame(board.getSquares(), p1, p2);
        solver = new Solver(board);
        fillPlayerRack(p1, bag);
        fillPlayerRack(p2, bag);
    }
    public static void makeMove(Player player) {
        int score = solver.makeBestMove(player);
        if(score == 0) resetPlayerRack(player, bag);
        else fillPlayerRack(player, bag);
        frame.repaint();
    }
    public static void fillPlayerRack(Player player, Bag bag) {
        int lettersNeeded = player.getLettersNeeded();
        for(int i = 1; i <= lettersNeeded; i++) {
            player.addLetter(bag.remove());
        }
    }
    public static void resetPlayerRack(Player player, Bag bag) {
        int size = player.getRack().size();
        for(int i = size - 1; i >= 0; i--) {
            bag.add(player.removeLetter(player.getRack().get(i)));
        }
        fillPlayerRack(player, bag);
    }

    public static int getLetterValue(char letter) {
        return LETTER_VALUES.get(letter);
    }
    static {
        LETTER_VALUES = new HashMap <Character,Integer>();
        char[] ALPHABET = null;
        int[] ALPH_POINTS = null;
        switch(Scrabble.LANGUAGE.toLowerCase()) {
            case "german":
            ALPHABET = new char[] {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','%','Ä','Ü','Ö'};
            ALPH_POINTS = new int[] { 1 , 3 , 4 , 1 , 1 , 4 , 2 , 2 , 1 , 6 , 4 , 2 , 3 , 1 , 2 , 4 , 10, 1 , 1 , 1 , 1 , 6 , 3 , 8 , 10, 3 , 0 , 6 , 6 , 8 };
                break;
            case "romanian":
                ALPHABET =  new char[] {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','Ă','Â','Î','Ș','Ț','%'};
                ALPH_POINTS = new int[] { 1 , 9 , 4 , 2 , 1 , 4 , 6 , 4 , 1 , 8 , 10, 1 , 3 , 1 , 1 , 3 , 10, 1 , 1 , 1 , 1 , 5 , 10, 10, 10, 10, 2 , 5 , 5 , 2 , 2 , 0 };
                break;
            case "english": 
            default:
                ALPHABET = new char[]   {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','%'};
                ALPH_POINTS = new int[] { 1 , 3 , 3 , 2 , 1 , 4 , 2 , 4 , 1 , 8 , 5 , 1 , 3 , 1 , 1 , 3 , 10, 1 , 1 , 1 , 1 , 4 , 4 , 8 , 4 , 10, 0};
                break;
        }
        for(int i = 0; i < ALPHABET.length; i++) {
            LETTER_VALUES.put(ALPHABET[i],ALPH_POINTS[i]);
        }
    }
}
