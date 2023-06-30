package com.example.Scrabble.Model.ScrabbleDictionary.CacheManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The IOSearcher class provides utility methods for searching words in files.
 */
public class IOSearcher {

    /**
     * Searches for a given word in the specified files.
     *
     * @param word      the word to search for
     * @param fileNames the filenames of the files to search in
     * @return true if the word is found in any of the files, false otherwise
     */
    public static boolean search(String word, String... fileNames) {
        for (String s : fileNames) {
            File file = new File(s);

            try {
                Scanner scanner = new Scanner(file);

                int lineNum = 0;
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    lineNum++;
                    if (line.contains(word)) {
                        scanner.close();
                        return true;
                    }
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                return false;
            }
        }
        return false;
    }
}
