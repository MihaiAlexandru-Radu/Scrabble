package src;

import java.util.ArrayList;

public class Solver {
    private Trie trie;
    private Player player;
    private Square[][] squares;
    private boolean[][] anchors;
    private int[][] crossScores;

    private String bestHorizontalMove;
    private String bestVerticalMove;
    private int bestHorizontalScore;
    private int bestVerticalScore;
    private boolean isHorizontal;

    public Solver(Board board) {
        this.trie = new Trie();
        this.player = null;
        this.squares = board.getSquares();
        this.anchors = new boolean[Board.ROWS][Board.COLS];
        this.crossScores = new int[Board.ROWS][Board.COLS];
        for(int row = 0; row < anchors.length; row++) {
            for(int col = 0; col < anchors.length; col++) {
                anchors[row][col] = false;
                crossScores[row][col] = 0;
            }
        }
        this.bestHorizontalMove = "";
        this.bestVerticalMove = "";
        this.bestHorizontalScore = 0;
        this.bestVerticalScore = 0;
        this.isHorizontal = false;
    }
    /**
     * Resets board statistics (anchors/crosschecks) to allow solver to find moves in new direction (horizontal/vertical)
     */
    public void findBestDirectedMove() {
        findAnchors(0, Board.ROWS-1);
        findCrossChecks(0, Board.ROWS-1);
        makeMove();
        transposeAll(); 
    }
    /**
     * At the start of every turn, function is called, finding two best moves: one with horizontal word and
     * other with vertical word. It then places the tiles where it finds the best move overall
     * @param player Current player making the move
     * @return highest score possible overall
     */
    public int makeBestMove(Player player) {
        this.bestHorizontalScore = 0;
        this.bestVerticalScore = 0;
        this.player = player;
        isHorizontal = true;
        findBestDirectedMove();
        isHorizontal = false;
        findBestDirectedMove();
        String word = "";
        int lastCol = 0, row = 0;
        isHorizontal = (bestHorizontalScore > bestVerticalScore);
        String bestMoveString = "";
        int bestScore = 0;
        if(isHorizontal && bestHorizontalScore > 0) {
            bestMoveString = bestHorizontalMove;
            bestScore = bestHorizontalScore;
        } else if(bestVerticalScore > 0) {
            bestMoveString = bestVerticalMove;
            bestScore = bestVerticalScore;
            transposeAll();
        }
        String[] bestMove = bestMoveString.split(",");
        player.addScore(bestScore);
        word = bestMove[0];
        if(bestScore != 0) {
            row = Integer.parseInt(bestMove[1]);
            lastCol = Integer.parseInt(bestMove[2]);
        }
        Panel.moveDesc = "-    Rack: " + player.getRack() + "   -   " + word + ": " + Math.max(bestHorizontalScore, bestVerticalScore);
        for (int col = lastCol; col >= lastCol - word.length() + 1; col--) {
            Character l = word.charAt(word.length() - (lastCol - col) - 1);
            // if tile is already full, skip
            if(!squares[row][col].isEmpty()) continue;
            if (!Character.isLowerCase(l)) { // if tile is letter
                player.removeLetter(l);
                squares[row][col].placeLetter(l, Scrabble.getLetterValue(l));
            } else { // if tile is blank
                player.removeLetter('%');
                squares[row][col].placeLetter(Character.toUpperCase(l), 0);
            }
        }
        if(bestVerticalScore > 0 && bestVerticalScore >= bestHorizontalScore) transposeAll();            
        this.bestHorizontalMove = "";
        this.bestVerticalMove = "";
        return Math.max(bestHorizontalScore, bestVerticalScore);
    }
    /**
     * Cycles through board and finds score of best move, will only check horizontal moves or vertical moves at a time
     * @return highest score possible in given direction
     */
    public int makeMove() {
        int bestScore = 0;
        for(int row = 0; row < Board.ROWS; row++) {
            int nonAnchorSquares = 0;
            for (int col = 0; col < Board.COLS - 1; col++) {
                if (!anchors[row][col]) nonAnchorSquares++;
                else {
                    if (col > 0 && squares[row][col - 1].isEmpty()) { 
                        // if left-adjacent tile is empty, we can make both left prefix and right side of word
                        leftPart("", trie.root, nonAnchorSquares, squares[row][col]);
                    } else if (col == 0) { 
                        // if right on the edge, only right side
                        extendRight("", trie.root, squares[row][col], squares[row][col]);
                    } else { 
                        // if in middle, must find prefix before it, and start partialWord and node accordingly
                        String prefix = "";
                        Node node = trie.root;
                        for (int i = col; i > 0; i--) {
                            if (squares[row][i].isEmpty())
                                break;
                            prefix = squares[row][i].getLetter() + prefix;
                            node = node.edges.get(squares[row][i].getLetter());
                        }
                        if (node != null) { // if no way to make word after prefix, we should immediately stop
                            extendRight(prefix, node, squares[row][col], squares[row][col]);
                        }
                    }
                    nonAnchorSquares = 0;
                }
            }
        }
        return bestScore;
    }
    /**
     * Creates left part to word, where no need to check for cross checks, essentially prefix to extendRight() 
     * @param partialWord current word created
     * @param n current node in trie, starts at root
     * @param limit starts as number of non-anchor square to left of anchorSquare
     * @param anchorSquare left-most anchor square in current row
     */
    public void leftPart(String partialWord, Node n, int limit, Square anchorSquare) {
        extendRight(partialWord, n, anchorSquare, anchorSquare);
        if(limit > 0) {
            for(Character l : n.edges.keySet()) {
                if(player.contains(l) || player.contains('%')) {
                    Node newNode = n.edges.get(l);
                    Character removedLetter = (player.contains(l)) ? player.removeLetter(l) : player.removeLetter('%');
                    if(removedLetter == '%') removedLetter = Character.toLowerCase(l);
                    leftPart(partialWord + removedLetter, newNode, limit-1, anchorSquare);
                    player.addLetter((Character.isLowerCase(removedLetter)) ? '%' : l);
                } 
            }
        }
    }
    /**
     * Goes left to right and proceduraly adds letters to word, if full word found, legalMove() scores it accordingly
     * @param partialWord current word created
     * @param n current node in trie 
     * @param square current square in traversal
     * @param anchorSquare square that MUST be filled for move to be legal
     */
    public void extendRight(String partialWord, Node n, Square square, Square anchorSquare) {
        if(square.isEmpty()) {
            if(n.terminal && square != anchorSquare) { 
                legalMove(partialWord, square.getRow(), square.getCol()-1);
            }
            for(Character l : n.edges.keySet()) {
                if(square.getCol() < Board.COLS - 1 && (player.contains(l) || player.contains('%')) && square.containsCrossCheck(l)) {
                    Node newNode = n.edges.get(l);
                    Square nextSquare = squares[square.getRow()][square.getCol()+1];
                    Character removedLetter = (player.contains(l)) ? player.removeLetter(l) : player.removeLetter('%');
                    if(removedLetter == '%') removedLetter = Character.toLowerCase(l);
                    extendRight(partialWord + removedLetter, newNode, nextSquare, anchorSquare);
                    player.addLetter((Character.isLowerCase(removedLetter)) ? '%' : l);
                }
            }
        } else {
            Character l = square.getLetter();
            if(square.getCol() < Board.COLS - 1 && n.edges.containsKey(l)) {
                Node newNode = n.edges.get(l);
                Square nextSquare = squares[square.getRow()][square.getCol()+1];
                if(square.getLetterScore() == 0) l = Character.toLowerCase(l);
                extendRight(partialWord + l, newNode, nextSquare, anchorSquare);
            }
        }
    }
    /**
     * Once finding a legal move, legalMove() is called, scoring it 
     * @param word full legal word found
     * @param row row word takes place in
     * @param col column location representing location of last word tile
     */
    public void legalMove(String word, int row, int col) {
        boolean leftClear = (col-word.length()+1 == 0 || squares[row][col-word.length()].isEmpty());
        boolean rightClear = (col == Board.COLS - 1 || squares[row][col+1].isEmpty());
        if(!leftClear || !rightClear) return; //if legal word illegaly adjacent to another word, NOT LEGAL
        int crossScore = 0;
        int wordScore = 0, wordMultiplier = 1;
        for(int checkCol = col; checkCol >= col-word.length()+1; checkCol--) {
            Square currentSquare = squares[row][checkCol];
            char currentLetter = word.charAt(word.length() - (col-checkCol) - 1);
            //add current letter value to word score, with letter multiplier if necessary
            int letterMultiplier = currentSquare.getLetterMultiplier();
            int letterScore = (Character.isLowerCase(currentLetter)) ? 0 : Scrabble.getLetterValue(currentLetter);
            wordScore += letterScore * letterMultiplier;
            //increase word multiplier if word multiplier tile present
            int currentWordMultiplier = currentSquare.getWordMultiplier();
            wordMultiplier *= currentWordMultiplier;
            //add score from cross words, only if crossword is possible
            if(currentSquare.isEmpty() && currentSquare.containsCrossWord()) {
                crossScore += (crossScores[row][checkCol] + (letterScore*letterMultiplier)) * currentWordMultiplier;
            }
        }
        int totalScore = (wordScore * wordMultiplier) + crossScore;
        if(word.length() == 7) totalScore += 50; //BINGO
        if(isHorizontal && totalScore > bestHorizontalScore) {
            bestHorizontalScore = totalScore;
            bestHorizontalMove = word + "," + row + "," + col;
        }
        if(!isHorizontal && totalScore > bestVerticalScore) {
            bestVerticalScore = totalScore;
            bestVerticalMove = word + "," + row + "," + col;
        }
    }
    /**
     * Find all anchor squares from startRow to endRow, inclusive
     * @param startRow search starting row
     * @param endRow search final row
     * @see AnchorSquare: all empty squares adjacent to a filled square   
     */
    public void findAnchors(int startRow, int endRow) {
        // if first move, only center tile is anchor square
        if(squares[Board.ROWS/2][Board.COLS/2].isEmpty()) {
            anchors[Board.ROWS/2][Board.COLS/2] = true;
            return;
        }
        // loop all squares, anchors defined as empty squares adjacent to filled squares
        for(int row = startRow; row <= endRow; row++) {
            for(int col = 0; col < Board.COLS; col++) {
                boolean isAdjacent = (row > 0 && !squares[row-1][col].isEmpty()) || //up tile
                                     (row < Board.ROWS - 1 && !squares[row+1][col].isEmpty()) || //down tile
                                     (col > 0 && !squares[row][col-1].isEmpty()) || //left tile
                                     (col < Board.COLS - 1 && !squares[row][col+1].isEmpty()); //right tile
                boolean isEmpty = squares[row][col].isEmpty();
                anchors[row][col] = (isAdjacent && isEmpty); //anchor only if both adjacent and empty
            }
        }
    }
    /**
     * Calculate all cross checks and cross scores from startRow to endRow, inclusive 
     * @param startRow search starting row
     * @param endRow search ending row
     * @see CrossCheck: all letters which can be legally placed in a given square
     * @see CrossScore: score gained from vertical words if letter placed in given square
     */
    public void findCrossChecks(int startRow, int endRow) {
        for(int row = startRow; row <= endRow; row++) {
            int checkRow = row; //row to be iterated
            for(int col = 0; col < Board.COLS; col++) {
                crossScores[row][col] = 0;
                if(!squares[row][col].isEmpty()) continue; //if square filled, no crosscheck
                int crossSum = 0;
                String prefix = ""; 
                // find prefix, the line of adjacent letters directly above current square
                while(checkRow-1 >= 0 && !squares[checkRow-1][col].isEmpty()) {
                    prefix = squares[checkRow-1][col].getLetter() + prefix;
                    crossSum += squares[checkRow-1][col].getLetterScore();
                    checkRow--;
                }
                String suffix = "";
                checkRow = row;
                // find suffix, the line of adjacent letters dirrectly below current square
                while(checkRow+1 < Board.ROWS && !squares[checkRow+1][col].isEmpty()) {
                    suffix = suffix + squares[checkRow+1][col].getLetter();
                    crossSum += squares[checkRow+1][col].getLetterScore();
                    checkRow++;
                }
                crossScores[row][col] = crossSum;
                // crossword score  possible only if prefix or suffix present to add score to 
                squares[row][col].setHasCrossWord(!prefix.isEmpty() || !suffix.isEmpty());
                if(prefix.isEmpty() && suffix.isEmpty()) {
                    squares[row][col].addDefaultCrossCheck();
                } else {
                //else if has prefix/suffix, add letters which correspond to tree availability
                    ArrayList<Character> crossCheck = new ArrayList<Character>();
                    for(char c = 'A'; c <= 'Z'; c++) {
                        String word = prefix + c + suffix;
                        if(trie.contains(word)) {
                            crossCheck.add(c);
                        }
                    }
                    squares[row][col].addCrossCheck(crossCheck);
                }
            }
        }
    }
    /**
     * When making vertical moves, transpose all data to allow up/down directions in algorithm
     */
    public void transposeAll() {
        Square[][] transposedSquares = new Square[Board.ROWS][Board.COLS];
        boolean[][] transposedAnchors = new boolean[Board.ROWS][Board.COLS];
        int[][] transposedCrossScores = new int[Board.ROWS][Board.COLS];
        for(int row = 0; row < Board.ROWS; row++) {
            for(int col = 0; col < Board.COLS; col++) {
                transposedSquares[col][row] = squares[row][col];
                transposedAnchors[col][row] = anchors[row][col];
                transposedCrossScores[col][row] = crossScores[row][col];
                transposedSquares[col][row].transposeCoordinates();                
            }
        }
        squares = transposedSquares;
        anchors = transposedAnchors;
        crossScores = transposedCrossScores;
    }
}
