package src;

public class Board {
    public static final int COLS = 15, ROWS = 15;
    public static final int SIZE = 35;

    private Square[][] squares;

    public Board() {
        initBoard();
    }
    public Square[][] getSquares() {
        return squares;
    }
    private void initBoard() {
        final String[][] TILE_DISTR = {{"TW","  ","  ","DL","  ","  ","  ","TW","  ","  ","  ","DL","  ","  ","TW"},
                                       {"  ","DW","  ","  ","  ","TL","  ","  ","  ","TL","  ","  ","  ","DW","  "},
                                       {"  ","  ","DW","  ","  ","  ","DL","  ","DL","  ","  ","  ","DW","  ","  "},
                                       {"DL","  ","  ","DW","  ","  ","  ","DL","  ","  ","  ","DW","  ","  ","DL"},
                                       {"  ","  ","  ","  ","DW","  ","  ","  ","  ","  ","DW","  ","  ","  ","  "},
                                       {"  ","TL","  ","  ","  ","TL","  ","  ","  ","TL","  ","  ","  ","TL","  "},
                                       {"  ","  ","DL","  ","  ","  ","DL","  ","DL","  ","  ","  ","DL","  ","  "},
                                       {"TW","  ","  ","DL","  ","  ","  ","DW","  ","  ","  ","DL","  ","  ","TW"},
                                       {"  ","  ","DL","  ","  ","  ","DL","  ","DL","  ","  ","  ","DL","  ","  "},
                                       {"  ","TL","  ","  ","  ","TL","  ","  ","  ","TL","  ","  ","  ","TL","  "},
                                       {"  ","  ","  ","  ","DW","  ","  ","  ","  ","  ","DW","  ","  ","  ","  "},
                                       {"DL","  ","  ","DW","  ","  ","  ","DL","  ","  ","  ","DW","  ","  ","DL"},
                                       {"  ","  ","DW","  ","  ","  ","DL","  ","DL","  ","  ","  ","DW","  ","  "},
                                       {"  ","DW","  ","  ","  ","TL","  ","  ","  ","TL","  ","  ","  ","DW","  "},
                                       {"TW","  ","  ","DL","  ","  ","  ","TW","  ","  ","  ","DL","  ","  ","TW"}};
        squares = new Square[ROWS][COLS];
        for(int row = 0; row < ROWS; row++) {
            for(int col = 0; col < COLS; col++) {
                squares[row][col] = new Square(row, col, TILE_DISTR[row][col]);
            }
        }
    }
}
