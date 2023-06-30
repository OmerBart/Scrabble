package com.example.Scrabble.Model.ScrabbleDictionary.CacheManager;

import java.util.HashSet;

/**
 * The CacheManager class is responsible for managing a cache of words with a specified maximum size and replacement policy.
 * It allows querying if a word is present in the cache and adding new words to the cache. When the cache is full, it applies
 * the specified replacement policy to make space for new words.
 */
public class CacheManager {
    CacheSize cacheSize;
    CacheReplacementPolicy cachePolicy;
    HashSet<String> cachedWords = new HashSet<>();

    /**
     * A helper class to manage the size of the cache.
     */
    private static class CacheSize {
        int max_size = 0;
        int current_size = 0;

        /**
         * Constructs a CacheSize object with the specified maximum size.
         *
         * @param max_size the maximum size of the cache
         */
        CacheSize(int max_size) {
            this.max_size = max_size;
        }

        /**
         * Increases the current size of the cache.
         */
        public void addCache() {
            current_size++;
        }

        /**
         * Checks if the cache has reached its maximum size.
         *
         * @return true if the cache is full, false otherwise
         */
        public boolean isMax() {
            return current_size >= max_size;
        }
    }

    /**
     * Constructs a CacheManager object with the specified maximum size and cache replacement policy.
     *
     * @param maxSize the maximum size of the cache
     * @param crp     the cache replacement policy
     */
    CacheManager(int maxSize, CacheReplacementPolicy crp) {
        cacheSize = new CacheSize(maxSize);
        cachePolicy = crp;
    }

    /**
     * Queries if a word is present in the cache.
     *
     * @param word the word to query
     * @return true if the word is present in the cache, false otherwise
     */
    public boolean query(String word) {
        return cachedWords.contains(word);
    }

    /**
     * Adds a word to the cache. If the cache is full, it applies the cache replacement policy to make space for the new word.
     *
     * @param word the word to add to the cache
     */
    public void add(String word) {
        if (!cacheSize.isMax()) {
            cacheSize.addCache();
        } else {
            cachedWords.remove(cachePolicy.remove());
        }
        cachePolicy.add(word.trim());
        cachedWords.add(word.trim());
    }
}
