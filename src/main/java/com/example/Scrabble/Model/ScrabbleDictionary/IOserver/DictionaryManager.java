package com.example.Scrabble.Model.ScrabbleDictionary.IOserver;


import com.example.Scrabble.Model.ScrabbleDictionary.CacheManager.Dictionary;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class DictionaryManager {
    LinkedHashMap<String, Dictionary> map;


    private static DictionaryManager single_instance = null;

    private DictionaryManager() {
        map = new LinkedHashMap<>();
    }


    public static DictionaryManager get() {
        if (single_instance == null)
            single_instance = new DictionaryManager();
        return single_instance;
    }


    public boolean query(String... Args) {
        AtomicBoolean flag = new AtomicBoolean(false);
        String word = Args[Args.length - 1];
        Arrays.stream(Args).filter(books -> books != word).forEach(books -> {
            if (!map.containsKey(books)) {
                map.put(books, new Dictionary(books));
            }
            if (map.get(books).query(word)) {
                flag.set(true);
            }
        });
//        for (String book : Args) {
//
//            if (!map.containsKey(book)) {
//                //System.out.println(book);
//                map.put(book, new Dictionary(book));
//            }
//            if (map.get(book).query(word)) {
//                flag = true;
//            }
//            if (book.equals(word))
//                break;
//
//        }
        return flag.get();
    }

    public boolean challenge(String... Args) {
        if (query(Args)) {
            String word = Args[Args.length - 1];
            for (String book : Args)
                if (map.get(book).challenge(word))
                    return true;
            return false;

        }
//        boolean flag = false;
//        String word = Args[Args.length-1];
//        for(String book : Args){
//            if(book.equals(word))
//                break;
//            if(!map.containsKey(book)){
//                map.put(book,new Dictionary(book));
//            }
//            if(map.get(book).query(word)){
//                flag = true;
//            }
//        }
//        return flag;
        return false;
    }

    public int getSize() {
        return map.size();
    }
}
