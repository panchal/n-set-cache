/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nsetcache;

/**
 * Sample client cache implementation based on NSetCache library.
 * 
 * @author panchal
 */
public class ClientCache extends NSetCache {
    
    /**
     * Constructor. Initializes N-way associative cache of numSet*numEntry size.
     * where, N = numEntry
     * with CUSTOM  replacement algorithm.
     * 
     * @param numSet
     * @param numEntry
     * @param replacementAlgo 
     */
    public ClientCache(int numSet, int numEntry, String replacementAlgo) {
        super(numSet, numEntry, replacementAlgo);
    }
    /**
     * Custom hash algorithm written for demo.
     * This uses standard java Object hashcode() function for hashing.
     * 
     * @param key Key.
     * @return Hashed integer Key.
     */
    @Override
    public int getHash(Object key) {        
        return Math.abs(key.hashCode());
    }
    
    /**
     * Custom replacement algorithm written for demo.
     * Simple algorithm, evicted index is always last index of Index Set.
     * 
     * @param startIndex Start index.
     * @param endIndex End index.
     * @return Index to be evicted.
     */
    @Override
    public int customReplacementAlgo(int startIndex, int endIndex) {
        return endIndex;
    }
    
}
