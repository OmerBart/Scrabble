package com.example.Scrabble.Model.ScrabbleDictionary.CacheManager;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Dictionary {
    CacheManager newCache;
    CacheManager oldCache;
    BloomFilter bf;
    String[] filenames = null;

    //
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
                //now read the file line by line...
                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase().trim();
                    //System.out.println("from dicts word: " + word);
                    word = word.toLowerCase().trim();
                    newCache.add(word);
                    bf.add(word);

                }
                scanner.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

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

    public boolean query(String word) {
        word = word.toLowerCase().trim();
        //System.out.println("from dicts word: " + word);
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
