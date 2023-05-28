package com.example.Scrabble.Model.ScrabbleDictionary.CacheManager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Objects;

public class BloomFilter {

    final BitSet bitSet;
    ArrayList<MessageDigest> md = new ArrayList<>(); // a list to hold the message digest algorithms

    // Constructor to create a new BloomFilter with a given set size and an array of
    // Strings to add to the filter
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

    // A method to print the contents of the bitSet to the console
    public void printBits() {
        System.out.println(this.toString());
    }

    // A method to add a String to the BloomFilter
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

    // A method to check if a given String is in the BloomFilter
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

    // Override the equals() method to compare the bitSet and ArrayList of message
    // digests between two BloomFilter objects
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BloomFilter that = (BloomFilter) o;
        return Objects.equals(bitSet, that.bitSet) && Objects.equals(md, that.md);
    }

    // Override the toString() method to return a String representation of the
    // bitSet
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

// package test;
// import java.math.BigInteger;
// import java.security.MessageDigest;
// import java.security.NoSuchAlgorithmException;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.BitSet;
// import java.util.Objects;
//
// public class BloomFilter {
//
// final BitSet bitSet;
// ArrayList<MessageDigest> md = new ArrayList<>();
//
//
// public BloomFilter(int setSize, String... values) {
//
// bitSet = new BitSet(setSize);
//
// for (String s : values) {
// try {
// md.add(MessageDigest.getInstance(s));
// } catch (NoSuchAlgorithmException e) {
// throw new RuntimeException(e);
// }
//
// }
//
//
// }
// public void printBits() {
// System.out.println(this.toString());
// }
//
// public void add(String w) {
// for(MessageDigest m : md){
// BigInteger bi = new BigInteger(m.digest(w.getBytes()));
// bitSet.set((Math.abs(bi.intValue()) % bitSet.size()),true);
// }
// }
//
// public boolean contains(String w) {
// for (MessageDigest m : md) {
// BigInteger bi = new BigInteger(m.digest(w.getBytes()));
// if (!bitSet.get(Math.abs(bi.intValue()) % bitSet.size()))
// return false;
// }
// return true;
// }
//
// @Override
// public boolean equals(Object o) {
// if (this == o) return true;
// if (o == null || getClass() != o.getClass()) return false;
// BloomFilter that = (BloomFilter) o;
// return Objects.equals(bitSet, that.bitSet) && Objects.equals(md, that.md);
// }
//
// @Override
// public String toString() {
// StringBuilder s = new StringBuilder();
// for (int i = 0; i < bitSet.length(); i++) {
// s.append(bitSet.get(i) ? 1 : 0);
// }
// return s.toString();
// }
//
// }
