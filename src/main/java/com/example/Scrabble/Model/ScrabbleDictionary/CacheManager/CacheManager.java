package com.example.Scrabble.Model.ScrabbleDictionary.CacheManager;

import java.util.HashSet;

public class CacheManager {
    CacheSize cacheSize;
    CacheReplacementPolicy cachePolicy;
    HashSet<String> cachedWords = new HashSet<>();

    private static class CacheSize {
        int max_size = 0;
        int current_size = 0;

        CacheSize(int max_size) {
            this.max_size = max_size;
        }

        public void addCache() {
            current_size++;
        }

        public boolean isMax() {
            if (current_size >= max_size)
                return true;
            else
                return false;
        }

    }

    CacheManager(int maxSize, CacheReplacementPolicy crp) {
        cacheSize = new CacheSize(maxSize);
        cachePolicy = crp;
    }

    public boolean query(String word) {
        return cachedWords.contains(word);
    }

    public void add(String word) {
        if (!cacheSize.isMax()) {
            cacheSize.addCache();
        } else {
            cachedWords.remove(cachePolicy.remove());
        }
        cachePolicy.add(word);
        cachedWords.add(word);

    }

}
