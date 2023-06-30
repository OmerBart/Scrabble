package com.example.Scrabble.Model.ScrabbleDictionary.CacheManager;

import java.util.LinkedHashSet;

/**
 * The LRU (Least Recently Used) class implements the CacheReplacementPolicy interface and represents a cache
 * replacement policy based on the least recently used algorithm.
 */
public class LRU implements CacheReplacementPolicy {

    LinkedHashSet<String> test;

    /**
     * Constructs a new LRU object.
     */
    public LRU() {
        this.test = new LinkedHashSet<>();
    }

    /**
     * Adds a word to the LRU cache.
     *
     * @param word the word to add
     */
    @Override
    public void add(String word) {
        // If the word is already present, remove it and add it back to update its position as the most recently used word.
        // This ensures that the LinkedHashSet maintains the order of least recently used to most recently used.
        test.remove(word);
        test.add(word);
    }

    /**
     * Removes the least recently used word from the LRU cache.
     *
     * @return the removed word
     */
    @Override
    public String remove() {
        String leastUsed = test.iterator().next();
        test.remove(leastUsed);
        return leastUsed;
    }
}
