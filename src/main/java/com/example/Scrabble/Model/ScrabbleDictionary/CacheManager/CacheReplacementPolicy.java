package com.example.Scrabble.Model.ScrabbleDictionary.CacheManager;

public interface CacheReplacementPolicy{
	void add(String word);
	String remove(); 
}
