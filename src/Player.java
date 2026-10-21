package src;

import java.util.ArrayList;

public class Player {
    public static final int RACK_MAX = 7;
    
    private ArrayList<Character> rack;
    private int score;

    public Player() {
        this.rack = new ArrayList<Character>();
        this.score = 0;
    }
    public void addLetter(Character letter) {
        rack.add(letter);
    }
    public Character removeLetter(Character letter) {
        rack.remove(letter);
        return letter;
    }
    public void addScore(int points) {
        score += points;
    }
    public int getLettersNeeded() {
        return RACK_MAX - rack.size();
    }
    public ArrayList<Character> getRack() {
        return rack;
    }
    public int getScore() {
        return score;
    }
    public boolean contains(Character letter) {
        return rack.contains(letter);
    }
}
