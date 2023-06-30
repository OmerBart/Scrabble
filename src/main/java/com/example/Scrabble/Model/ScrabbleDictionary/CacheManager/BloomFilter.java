package com.example.Scrabble.Model.ScrabbleDictionary.CacheManager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Objects;

/**
 * A Bloom filter is a space-efficient probabilistic data structure used to test whether an element is a member of a set.
 * This implementation uses a BitSet to represent the filter and a list of MessageDigest algorithms to generate hash values.
 */
public class BloomFilter {

    final BitSet bitSet;
    ArrayList<MessageDigest> md = new ArrayList<>(); // a list to hold the message digest algorithms

    /**
     * Constructs a new BloomFilter with the specified set size and adds the given values to the filter.
     *
     * @param setSize the size of the Bloom filter set
     * @param values  the values to add to the filter
     */
    public BloomFilter(int setSize, String... values) {
        bitSet = new BitSet(setSize); // Initialize a new BitSet of the given size

        // For each String in the input array, create a new instance of MessageDigest
        // using the given algorithm and add it to the ArrayList
        for (String s : values) {
            try {
                md.add(MessageDigest.getInstance(s));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Prints the contents of the bitSet to the console.
     */
    public void printBits() {
        System.out.println(this.toString());
    }

    /**
     * Adds a String to the BloomFilter.
     *
     * @param w the String to add
     */
    public void add(String w) {
        // For each MessageDigest in the ArrayList, create a new BigInteger from the
        // digest of the input String using that MessageDigest
        // Then, set the bit in the bitSet at the position given by the modulus of the
        // BigInteger and the size of the bitSet
        for (MessageDigest m : md) {
            BigInteger bi = new BigInteger(m.digest(w.getBytes()));
            bitSet.set((Math.abs(bi.intValue()) % bitSet.size()), true);
        }
    }

    /**
     * Checks if a given String is in the BloomFilter.
     *
     * @param w the String to check
     * @return true if the String is possibly in the filter, false otherwise
     */
    public boolean contains(String w) {
        // For each MessageDigest in the ArrayList, create a new BigInteger from the
        // digest of the input String using that MessageDigest
        // Then, check if the bit in the bitSet at the position given by the modulus of
        // the BigInteger and the size of the bitSet is set to true
        // If any of the bits are not set to true, return false
        for (MessageDigest m : md) {
            BigInteger bi = new BigInteger(m.digest(w.getBytes()));
            if (!bitSet.get(Math.abs(bi.intValue()) % bitSet.size()))
                return false;
        }
        // If all bits are set to true, return true
        return true;
    }

    /**
     * Compares the current BloomFilter object with another object.
     *
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BloomFilter that = (BloomFilter) o;
        return Objects.equals(bitSet, that.bitSet) && Objects.equals(md, that.md);
    }

    /**
     * Returns a string representation of the BitSet.
     *
     * @return a string representation of the BitSet
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bitSet.length(); i++) {
            s.append(bitSet.get(i) ? 1 : 0); // Append 1 to the StringBuilder if the bit at the current index is set to
            // true, and 0 otherwise
        }
        return s.toString();
    }

}
