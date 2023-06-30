package com.example.Scrabble.Model.ScrabbleDictionary.CacheManager;

/**
 * The CacheReplacementPolicy interface defines the contract for a cache replacement policy used by the CacheManager class.
 * Implementing classes should provide the logic for adding a word to the policy and selecting a word to remove when the cache is full.
 */
public interface CacheReplacementPolicy {

	/**
	 * Adds a word to the cache replacement policy.
	 *
	 * @param word the word to add to the policy
	 */
	void add(String word);

	/**
	 * Selects a word to remove from the cache based on the defined policy.
	 *
	 * @return the word to remove from the cache
	 */
	String remove();
}
