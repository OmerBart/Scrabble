package test;


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
        bf = new BloomFilter(256,"MD5","SHA1");
        this.filenames = fileNames;

        for (String s : fileNames) {
            File file = new File(s);

            try {
                Scanner scanner = new Scanner(file);
                scanner.useDelimiter(" ");
                //now read the file line by line...
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    newCache.add(word);
                    bf.add(word);

                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public boolean challenge(String word) {
        try {

            if (!IOSearcher.search(word,filenames)) {
                oldCache.add(word);
                return false;
            } else {
                newCache.add(word);
                return true;
            }
        } catch (Exception e) {
            return false;
        }


    }

    public boolean query(String word) {
        if(newCache.query(word))
            return true;
        else if(oldCache.query(word))
            return false;
        else {
            if(!bf.contains(word)) {
                oldCache.add(word);
                return false;
            }
            else {
                newCache.add(word);
                return true;
            }
        }

    }
}
