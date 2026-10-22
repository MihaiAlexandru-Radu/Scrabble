package src;

import java.util.ArrayList;

public class Square {
    private int row, col;
    private int letterMultiplier, wordMultiplier;
    private char letter;
    private int letterScore;
    private String desc;
    private ArrayList<Character> crossCheck;
    private boolean hasCrossword;

    public Square(int row, int col, String desc) {
        this.row = row;
        this.col = col;
        this.letterMultiplier = 1;
        this.wordMultiplier = 1;
        this.letter = '-';
        this.letterScore = 0;
        this.desc = desc;
        this.crossCheck = new ArrayList<Character>();
        this.hasCrossword = false;
        switch(desc) {
            case "DL":
                this.letterMultiplier = 2;
                break;
            case "TL":
                this.letterMultiplier = 3;
                break;
            case "DW":
                this.wordMultiplier = 2;
                break;
            case "TW":
                this.wordMultiplier = 3;
                break;
        }
        addDefaultCrossCheck();
    }
    public void placeLetter(char letter, int letterScore) {
        this.letter = letter;
        this.letterScore = letterScore;
    }
    public void removeLetter() {
        this.letter = '-';
        this.letterScore = 0;
    }
    public char getLetter() {
        return letter;
    }
    public int getLetterScore() {
        return letterScore;
    }
    public boolean isEmpty() {
        return letter == '-';
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public int getLetterMultiplier() {
        return (isEmpty()) ? letterMultiplier : 1;
    }
    public int getWordMultiplier() {
        return (isEmpty()) ? wordMultiplier : 1;
    }
    public String getStringDesc() {
        return desc;
    }
    public boolean containsCrossCheck(Character letter) {
        return crossCheck.contains(letter);
    }
    public boolean containsCrossWord() {
        return hasCrossword;
    }
    public void setHasCrossWord(boolean hasCrossword) {
        this.hasCrossword = hasCrossword;
    }
    public void addDefaultCrossCheck() {
        ArrayList<Character> alphabet = new ArrayList<Character>();
        for(Character letter = 'A'; letter <= 'Z'; letter++) {
            alphabet.add(letter);
        }
        addCrossCheck(alphabet);
    }
    public void addCrossCheck(ArrayList<Character> other) {
        crossCheck.clear();
        crossCheck.addAll(other);
    }
    public void transposeCoordinates() {
        int temp = row;
        row = col;
        col = temp;
    }
}
