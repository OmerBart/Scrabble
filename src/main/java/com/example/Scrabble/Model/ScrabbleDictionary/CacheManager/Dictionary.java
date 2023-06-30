package com.example.Scrabble.Model.ScrabbleDictionary.CacheManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The Dictionary class represents a dictionary of words used for Scrabble or word-related games. It utilizes caching
 * mechanisms and a Bloom filter for efficient word lookups.
 */
public class Dictionary {
    CacheManager newCache;
    CacheManager oldCache;
    BloomFilter bf;
    String[] filenames = null;

    /**
     * Constructs a Dictionary object with the specified filenames representing the dictionary files.
     *
     * @param fileNames the filenames of the dictionary files
     */
    public Dictionary(String... fileNames) {
        newCache = new CacheManager(400, new LRU());
        oldCache = new CacheManager(100, new LFU());
        bf = new BloomFilter(32768, "MD5", "SHA1", "SHA-256", "SHA-512");
        this.filenames = fileNames;

        for (String s : fileNames) {
            File file = new File(s);

            try {
                Scanner scanner = new Scanner(file);
                scanner.useDelimiter(" ");
                // Read the file line by line
                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase().trim();
                    newCache.add(word);
                    bf.add(word);
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Checks if a word is a valid challenge by searching for it in the dictionary.
     * If the word is not found, it is added to the old cache.
     *
     * @param word the word to challenge
     * @return true if the word is found in the dictionary, false otherwise
     */
    public boolean challenge(String word) {
        try {
            word = word.toLowerCase().trim();

            if (!IOSearcher.search(word.trim(), filenames)) {
                oldCache.add(word.trim());
                return false;
            } else {
                newCache.add(word.trim());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Queries if a word is present in the dictionary. It first checks the new cache, then the old cache,
     * and if not found, performs a lookup using the Bloom filter.
     * If the word is not found, it is added to the old cache.
     *
     * @param word the word to query
     * @return true if the word is found in the dictionary, false otherwise
     */
    public boolean query(String word) {
        word = word.toLowerCase().trim();

        if (newCache.query(word))
            return true;
        else if (oldCache.query(word))
            return false;

        if (!bf.contains(word)) {
            oldCache.add(word);
            return false;
        } else {
            newCache.add(word);
            return true;
        }
    }
}
