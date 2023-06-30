package com.example.Scrabble.Model.ScrabbleDictionary.IOserver;

import com.example.Scrabble.Model.ScrabbleDictionary.CacheManager.Dictionary;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The `DictionaryManager` class manages dictionaries for Scrabble word operations.
 * It provides methods to query and challenge words in multiple dictionaries.
 * The class follows the singleton design pattern to ensure a single instance of the manager.
 */
public class DictionaryManager {
    LinkedHashMap<String, Dictionary> map;

    private static DictionaryManager singleInstance = null;

    private DictionaryManager() {
        map = new LinkedHashMap<>();
    }

    /**
     * Returns the singleton instance of the DictionaryManager.
     *
     * @return the DictionaryManager instance
     */
    public static DictionaryManager get() {
        if (singleInstance == null)
            singleInstance = new DictionaryManager();
        return singleInstance;
    }

    /**
     * Queries the dictionaries for the existence of a word.
     *
     * @param args the names of the dictionaries and the word to query
     * @return true if the word exists in any of the dictionaries, false otherwise
     */
    public boolean query(String... args) {
        AtomicBoolean flag = new AtomicBoolean(false);
        String word = args[args.length - 1];
        Arrays.stream(args)
                .filter(dictionaryName -> dictionaryName != word)
                .forEach(dictionaryName -> {
                    if (!map.containsKey(dictionaryName)) {
                        map.put(dictionaryName, new Dictionary(dictionaryName));
                    }
                    if (map.get(dictionaryName).query(word)) {
                        flag.set(true);
                    }
                });
        return flag.get();
    }

    /**
     * Checks if a word is a valid challenge by searching for it in the dictionaries.
     *
     * @param args the names of the dictionaries and the word to challenge
     * @return true if the word is a valid challenge in any of the dictionaries, false otherwise
     */
    public boolean challenge(String... args) {
        if (query(args)) {
            String word = args[args.length - 1];
            for (String dictionaryName : args) {
                if (map.get(dictionaryName).challenge(word)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Returns the number of dictionaries in the manager.
     *
     * @return the number of dictionaries
     */
    public int getSize() {
        return map.size();
    }
}
