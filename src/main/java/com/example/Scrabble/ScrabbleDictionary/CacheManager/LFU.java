package com.example.Scrabble.ScrabbleDictionary.CacheManager;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;

public class LFU implements CacheReplacementPolicy {
    // ArrayList<WordInfo> wak = new ArrayList<>();
    LinkedHashSet<WordInfo> wak;
    LinkedHashMap<String, Integer> wordHashMap;
    String lastLeaseUsed = null;
    WordInfo leastUsedWord = null;

    public LFU() {
        this.wordHashMap = new LinkedHashMap<>();
    }

    private static class WordInfo {
        String word;
        int wordUsed = 0;

        public WordInfo(String word) {
            this.word = word;
            this.wordUsed++;
        }

        public WordInfo(String word, int k) {
            this.word = word;
            this.wordUsed = k;
        }

        public String getWord() {
            return word;
        }

        public int getWordUsed() {
            return wordUsed;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            WordInfo wordInfo = (WordInfo) o;
            return Objects.equals(word, wordInfo.word);
        }

        @Override
        public int hashCode() {
            return Objects.hash(word, wordUsed);
        }
    }

    @Override
    public void add(String word) {
        if (!wordHashMap.containsKey(word)) {
            leastUsedWord = new WordInfo(word);
            wordHashMap.put(word, 0);
        } else {
            int a = wordHashMap.get(word);
            if (++a < leastUsedWord.getWordUsed())
                leastUsedWord = new WordInfo(word, ++a);
            wordHashMap.replace(word, ++a);
        }

    }

    @Override
    public String remove() {
        wordHashMap.remove(leastUsedWord.getWord());
        return leastUsedWord.getWord();

    }
}
