package com.example.Scrabble.ScrabbleServer;


import com.example.Scrabble.Server.CacheManager.Dictionary;

import java.util.LinkedHashMap;

public class DictionaryManager {
    LinkedHashMap<String, Dictionary> map;


    private static DictionaryManager single_instance = null;
    private DictionaryManager(){map = new LinkedHashMap<>();}


    public static DictionaryManager get(){
        if (single_instance == null)
            single_instance = new DictionaryManager();
        return single_instance;
    }


    public boolean query(String... Args) {
        boolean flag = false;
        String word = Args[Args.length-1];
        for(String book : Args){
            if(book.equals(word))
                break;
            if(!map.containsKey(book)){
                //System.out.println(book);
                map.put(book,new Dictionary(book));
            }
            if(map.get(book).query(word)){
                flag = true;
            }
        }
        return flag;
    }

    public boolean challenge(String...Args) {
        if(query(Args)){
            String word = Args[Args.length-1];
            for(String book: Args)
                if(map.get(book).challenge(word))
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
