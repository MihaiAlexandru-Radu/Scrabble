package Scrabble;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyReader implements KeyListener {
    private Player p1, p2;
    private boolean isPlayerOneTurn = true;

    public KeyReader(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(isPlayerOneTurn) {
                Scrabble.makeMove(p1);
            } else {
                Scrabble.makeMove(p2);
            }
            isPlayerOneTurn = !isPlayerOneTurn;
        } 
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    
}
