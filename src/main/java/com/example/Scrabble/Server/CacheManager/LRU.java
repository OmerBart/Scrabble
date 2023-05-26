package com.example.Scrabble.Server.CacheManager;

import java.util.LinkedHashSet;

public class LRU implements CacheReplacementPolicy {

    LinkedHashSet<String> test;

    public LRU() {
        this.test = new LinkedHashSet<>();
    }

    @Override
    public void add(String word) {

        // if word is new add it, if not remove it and add back with new key
        // this allows for the map to have the least recently used word with the
        // smallest key
        test.remove(word);
        test.add(word);
    }

    @Override
    public String remove() {
        String leastUsed = test.iterator().next();
        test.remove(leastUsed);
        return leastUsed;
    }
}
