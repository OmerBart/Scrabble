package com.example.Scrabble.Server.CacheManager;

public interface CacheReplacementPolicy{
	void add(String word);
	String remove(); 
}
