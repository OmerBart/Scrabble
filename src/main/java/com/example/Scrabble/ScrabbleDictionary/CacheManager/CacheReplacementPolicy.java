package com.example.Scrabble.ScrabbleDictionary.CacheManager;

public interface CacheReplacementPolicy{
	void add(String word);
	String remove(); 
}
