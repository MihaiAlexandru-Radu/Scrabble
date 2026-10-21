package src;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {

    private static final int HORIZONTAL_BORDER = 16;
	private static final int VERTICAL_BORDER = 59;

    public Frame(Square[][] squares, Player p1, Player p2) {
		this.add(new Panel(squares, p1, p2));
		this.pack();
    	this.setSize(Board.COLS * Board.SIZE + HORIZONTAL_BORDER, Board.ROWS * Board.SIZE + VERTICAL_BORDER);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Scrabble");
	}   
}
class Panel extends JPanel {
	
    private static final Color NO_LETTER_TILE = new Color(214,214,216),
							   LETTER_TILE = new Color(252,222,174),
							   DOUBLE_WORD_TILE = new Color(245,139,112),
							   TRIPLE_WORD_TILE = new Color(185,78,76),  
							   DOUBLE_LETTER_TILE = new Color(112,162,195),
							   TRIPLE_LETTER_TILE = new Color(38,104,158);

	private static final Font MULTIPLIER_FONT = new Font("Abadi",Font.PLAIN, 20),
							  LETTER_FONT = new Font("Abadi",Font.BOLD, 23),
							  NUMBER_FONT = new Font("Abadi",Font.BOLD, 10);

    private Square[][] squares;
	private Player p1, p2;
	public static String moveDesc = "";

    public Panel(Square[][] squares, Player p1, Player p2) {
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.addKeyListener(new KeyReader(p1, p2));
		this.squares = squares;
		this.p1 = p1;
		this.p2 = p2;
	}
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString("P1: " + p1.getScore() + "     " + 
					 "P2: " + p2.getScore() + "     " + 
					moveDesc, 5, Board.ROWS * Board.SIZE + 15);
		paintBoard(g);
	}
    private void paintBoard(Graphics g) {
        for(int row = 0; row < Board.ROWS; row++) {
			for(int col = 0; col < Board.COLS; col++) {
				paintSquare(g, squares[row][col]);
			}
		}
    }
    private void paintSquare(Graphics g, Square square) {
		Color fill = getFill(square);
		Color outline = Color.BLACK;
        int x = square.getCol() * Board.SIZE;
        int y = square.getRow() * Board.SIZE;
		g.setColor(fill);
		g.fillRoundRect(x, y, Board.SIZE, Board.SIZE, 10, 10);
		g.setColor(outline);
		g.drawRoundRect(x, y, Board.SIZE, Board.SIZE, 10, 10);
		if(square.isEmpty()) paintEmptySquare(g, square);
		else {
			if(square.getLetterScore() == 0) {
				paintBlankTileCircle(g, square);
				g.setColor(Color.WHITE);
			} else {
				paintNumber(g, x, y, square);
				g.setColor(Color.BLACK);
			}
			paintSquareLetter(g, x, y, square);

		}
	}
    private void paintSquareLetter(Graphics g, int x, int y, Square square) {
		int X_OFFSET = 7, Y_OFFSET = 25;
		final String UNIQUE_OFFSETS = "Q4,I15,L11,F11,J11,T11,V10,S10,Y10,Q6,D9,A9,R9,N9,T9,P11,B9,G9,U9,C9,O9,E10,X10,Z10,H9,Ä9,Ü9,Ă9,Â9,Î15,Ș10,Ț11,";
		String squareLetter = ""+square.getLetter();
		if(UNIQUE_OFFSETS.contains(squareLetter)) {
			X_OFFSET = Integer.parseInt(UNIQUE_OFFSETS.substring(UNIQUE_OFFSETS.indexOf(squareLetter)+1,UNIQUE_OFFSETS.indexOf(",", UNIQUE_OFFSETS.indexOf(squareLetter)+1)));
		}
		Font font = LETTER_FONT;
		g.setFont(font);
		g.drawString(squareLetter, x + X_OFFSET, y + Y_OFFSET);
		
	}
    private void paintNumber(Graphics g, int x, int y, Square square) {
		int letterValue = square.getLetterScore();
		Font font = NUMBER_FONT;
		g.setColor(Color.BLACK);
		g.setFont(font);
		if(letterValue < 10) g.drawString("" + letterValue, x  + 28, y  + 30);
		else g.drawString("" + letterValue, x  + 22, y  + 30);
	}
    private void paintEmptySquare(Graphics g, Square square) {
		final int WORD_MULTIPLIER_OFFSET = 1;
        int x = square.getCol() * Board.SIZE;
        int y = square.getRow() * Board.SIZE;
		Font font = MULTIPLIER_FONT;
		g.setFont(font);
		g.setColor(Color.WHITE);
		if(square.getWordMultiplier() > 1) {
			g.drawString(square.getStringDesc(), x + WORD_MULTIPLIER_OFFSET, y  + 25);
		}
		if(square.getLetterMultiplier() > 1) {
			g.drawString(" " + square.getStringDesc(), x , y  + 25);
		}
	}
    private void paintBlankTileCircle(Graphics g, Square square) {
		int X_OFFSET = 2, Y_OFFSET = 1;
		final int CIRCLE_RADIUS = 30;
		final String UNIQUE_OFFSETS = "Q12,T31,Q31,P11,Z11,W21,Ț31,";
		int x = square.getCol() * Board.SIZE;
        int y = square.getRow() * Board.SIZE;
		String squareLetter = square.getLetter() + "";
		if(UNIQUE_OFFSETS.contains(squareLetter)) {
			X_OFFSET = Integer.parseInt(UNIQUE_OFFSETS.substring(UNIQUE_OFFSETS.indexOf(squareLetter)+1,UNIQUE_OFFSETS.indexOf(squareLetter)+2));
			Y_OFFSET = Integer.parseInt(UNIQUE_OFFSETS.substring(UNIQUE_OFFSETS.indexOf(squareLetter)+2,UNIQUE_OFFSETS.indexOf(squareLetter)+3));
		}
		g.setColor(Color.BLACK);
		g.fillOval(x + X_OFFSET, y + Y_OFFSET, CIRCLE_RADIUS, CIRCLE_RADIUS);
	}
    private Color getFill(Square square) {
		if(!square.isEmpty()) return LETTER_TILE;
        else if(square.getLetterMultiplier() == 2) return DOUBLE_LETTER_TILE;
        else if(square.getLetterMultiplier() == 3) return TRIPLE_LETTER_TILE;
        else if(square.getWordMultiplier() == 2) return DOUBLE_WORD_TILE;
        else if(square.getWordMultiplier() == 3) return TRIPLE_WORD_TILE;
        else return NO_LETTER_TILE;
	}
}
