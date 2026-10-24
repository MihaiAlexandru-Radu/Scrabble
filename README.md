# Scrabble Solver

## Description
This program showcases a Scrabble algorithm. The game has no human players, but two AI's playing against each other, each playing the highest-scoring word based on the current board state. The program 
quits only when instructed two by the player. The AI's will eventually stop playing any more moves once they don't have enough letter tiles.

It should support everything from the original game of Scrabble from blank letter tiles to word and letter multiplier squares.

The central algorithm design is based off the explanation here: [The World’s Fastest Scrabble Program](https://www.cs.cmu.edu/afs/cs/academic/class/15451-s06/www/lectures/scrabble.pdf).

The paper gives pseudocode on generating words, but methods for actually storing and scoring the correct words (legalMove) are left to the reader. Similarly, though it explains anchors and cross-checks, designing an algorithm to actually find them is nonetheless a challenge.

## Usage
Once you have the program copied into a code editor (e.g. Visual Studio Code), run the main file Scrabble.java, which should open the visualized board. Once there, press space repeatedly for each of 
the AI's turns, until they no longer do turns. Close the Java window to quit the program.

The program also is able to play in German and Romanian. In the Scrabble.java file, change the line with any of the other two languages (in lower-case):
> public static final String LANGUAGE = "english";

> [!NOTE]
> The algorithm may show some weird two-letter cross words. This is normal because the Scrabble Dictionary has a lot of strange two-letter words to give players an easier time.

## Program Run Example
![Scrabble gameplay demo](/resources/scrabble_gameplay.gif)


## To-Do
Provide a local website to visualize the program on.
