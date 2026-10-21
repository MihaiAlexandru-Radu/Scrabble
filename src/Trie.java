package src;

import java.util.Map;
import java.util.HashMap;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Node {
        Map <Character,Node> edges;
        boolean terminal;

        public Node() {
            edges = new HashMap <Character,Node>();
            terminal = false;
        }
}

public class Trie {
    final Node root;

    public Trie() {
        root = new Node();
        addAllWords();
    }
    public void insert(String word) {
        Node pointer = root;
        for(char c: word.toCharArray()) {
            if(!pointer.edges.containsKey(c)) {
                pointer.edges.put(c, new Node());
            }
            pointer = pointer.edges.get(c);
        }
        pointer.terminal = true;
    }
    public boolean contains(String word) {
        Node pointer = root;
        for(char c: word.toCharArray()) {
            if(!pointer.edges.containsKey(c)) {
                return false;
            }
            pointer = pointer.edges.get(c);
        }
        return pointer.terminal;
    }
    private void addAllWords() {
        Path dictionaryPath = null;
        String dictionaryLink = "../resources/" + Scrabble.LANGUAGE.toLowerCase() + ".txt";
        dictionaryPath = Paths.get(dictionaryLink);
        try (BufferedReader dictionaryReader = Files.newBufferedReader(dictionaryPath, StandardCharsets.UTF_8)) {
            dictionaryReader.readLine();
            while (dictionaryReader.ready()) {
                String word = dictionaryReader.readLine().toUpperCase();
                insert(word);
            }
            dictionaryReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
