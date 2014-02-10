/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nsetcache;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * N-way set associative cache implementation Class.
 * 
 * @author panchal
 */
public class NSetCache implements Cache {

    public final CacheEntry[] cacheArray; 
    public final String algo;

    private final int numSet;
    private final int numEntry;
    
    /**
     * Constructor. Initializes direct mapped cache of numSet size.
     * with LRU as replacement algorithm.
     * 
     * @param numSet Number of set in cache.
     */
    public NSetCache(int numSet) {
        this.numSet = numSet;
        this.numEntry = 1;
        this.algo = "LRU";
        cacheArray = new CacheEntry[this.numSet*this.numEntry];
        clear();
    }
    
    /**
     * Constructor. Initializes N-way associative cache of numSet*numEntry size.
     * where, N = numEntry
     * with LRU as replacement algorithm.
     * 
     * @param numSet Number of set in cache.
     * @param numEntry Number of entries in each set.
     */
    public NSetCache(int numSet, int numEntry) {
        this.numSet = numEntry;
        this.numEntry = numEntry;
        this.algo = "LRU";
        cacheArray = new CacheEntry[this.numSet*this.numEntry];
        clear();
    }
    
    /**
     * Constructor. Initializes N-way associative cache of numSet*numEntry size.
     * where, N = numEntry
     * with user defined replacement algorithm. 
     * 
     * @param numSet Number of set in cache.
     * @param numEntry Number of entries in each set.
     * @param replacementAlgo Replacement algorithm switch. Supports LRU/MRU/CUSTOM.
     */
    public NSetCache(int numSet, int numEntry, String replacementAlgo) {
        this.numSet = numSet;
        this.numEntry = numEntry;
        this.algo = replacementAlgo;
        cacheArray = new CacheEntry[this.numSet*this.numEntry];
        clear();
    }
    
    public final Object get(Object key) {
        
        // Scan Policy.
        int intKey = getHash(key);
        int startIndex = getStartIndex(intKey);
        int endIndex = getEndIndex(startIndex);
        
        Object data = null;
        int emptyIndex = 0;
        boolean isSingleEntryEmpty = false;
        boolean cacheUpdated = false;
        for (int i = startIndex; i <= endIndex ; i++) {
            
            // if empty, continue.
            if (cacheArray[i].isEmpty() && !isSingleEntryEmpty) {
                emptyIndex = i;
                isSingleEntryEmpty = true;
                continue;
            } 
            
            // if hit, retrive data & update timestamp.
            if (cacheArray[i].getTag() == intKey) {
                data = cacheArray[i].data;
                cacheArray[i].setTimestamp(getCurrentTime());
                cacheUpdated = true;
                break;
            } 
        }
        
        // if miss, dummy retrieve data from main memory.
        // # DUMMY CODE BLOCK FOR FUTURE USAGE
        CacheEntry cacheEntryFromMM = null;
        if (!cacheUpdated) {
            cacheEntryFromMM = dummyCallMainMemory(key);
            data = cacheEntryFromMM.getData();
            cacheUpdated = true;
        }
        
        // if miss & empty entry location then update cache.
        // # DUMMY CODE BLOCK FOR FUTURE USAGE
        if (isSingleEntryEmpty) {
            //cacheArray[emptyIndex] = cacheEntryFromMM;
            cacheUpdated = true;
        }
        
        // if miss & no empty entry location, Replacement Policy used to evict
        // single entry to make space for new entry.
        // # DUMMY CODE BLOCK FOR FUTURE USAGE
        if (!cacheUpdated) {
            int evictedIndex = getEvictedIndex(algo, startIndex, endIndex);
            cacheArray[evictedIndex] = cacheEntryFromMM;
        }
        
        return data;
    }

    public final void put(Object key, Object value) {
        
        // Scan Policy.
        int intKey = getHash(key);
        int startIndex = getStartIndex(intKey);
        int endIndex = getEndIndex(startIndex);
        
        CacheEntry newCacheEntry = new CacheEntry(intKey, value, false, getCurrentTime());
        
        int emptyIndex = 0;
        boolean isSingleEntryEmpty = false;
        boolean cacheUpdated = false;
        for (int i = startIndex; i <= endIndex ; i++) {
            
            // if empty, note index & continue.
            if (cacheArray[i].isEmpty() && !isSingleEntryEmpty) {
                emptyIndex = i;
                isSingleEntryEmpty = true;
                continue;
            }
            
            // if hit, update cache.
            if (cacheArray[i].getTag() == intKey) {
                cacheArray[i] = newCacheEntry;
                cacheUpdated = true;
                break;
            } else {
                continue;
            }
        }
        
        // If miss, new entry is added to empty location.
        if (isSingleEntryEmpty) {
            cacheArray[emptyIndex] = newCacheEntry;
            cacheUpdated = true;
        }
        
        // If miss, If no empty location, Replacement Policy used to evict
        // single entry to make space for new entry.
        if (!cacheUpdated) {
            int evictedIndex = getEvictedIndex(algo, startIndex, endIndex);
            cacheArray[evictedIndex] = newCacheEntry;       
        }
    }

    public final int size() {
        return numSet*numEntry;
    }

    public final void clear() {
        for (int i=0; i<cacheArray.length; i++) {
            cacheArray[i] = new CacheEntry();
        }
    }
    
    /**
     * Hashing method defines hash algorithm for key mapping to cache address 
     * location. Default hash map implemented here is MD5 hash algorithm..
     * 
     * @param key Key.
     * @return Hashed integer Key.
     */
    public int getHash(Object key) {
        byte[] bytesOfKey = null;
        try {
            bytesOfKey = key.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NSetCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(NSetCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] hashBytes = md.digest(bytesOfKey);
        return Math.abs(byteArrayToInt(hashBytes));
    }
    
    /**
     * Returns start index for a given key of a Index Set.
     * 
     * @param intKey Key.
     * @return Start index.
     */
    private int getStartIndex(int intKey){
        return (intKey % numSet) * numEntry;
    }
    
    /**
     * Returns end index for a given key of a Index Set.
     * 
     * @param startIndex Start index
     * @return End index
     */
    private int getEndIndex(int startIndex){
        return startIndex + numEntry - 1;
    }

    /**
     * Generic method evaluating index to be evicted in case of all entries
     * occupied in order to make room for new entry. Based on switch.
     * 
     * @param algo Switch choose between LRU, MRU, CUSTOM replacement algorithm.
     * @param startIndex Start index.
     * @param endIndex End index.
     * @return Index to be evicted.
     */
    private int getEvictedIndex(String algo, int startIndex, int endIndex) {
        if ("LRU".equals(algo)) {
            return lruReplacementAlgo(startIndex, endIndex);
        } else if ("MRU".equals(algo)) {
            return mruReplacementAlgo(startIndex, endIndex);
        } else if ("CUSTOM".equals(algo)) {
            return customReplacementAlgo(startIndex, endIndex);
        } else {
            throw new UnsupportedOperationException("Unsupported Replacement alogorithm usage.");
        }
    }

    /**
     * LRU algorithm to evaluate index to be evicted.
     * 
     * @param startIndex Start index.
     * @param endIndex End index.
     * @return Index to be evicted.
     */
    private int lruReplacementAlgo(int startIndex, int endIndex) {
        int lruIndex = startIndex;
        long lruTimestamp = cacheArray[startIndex].getTimestamp();
        for (int i = startIndex; i <= endIndex ; i++) {
            long currentTimestamp = cacheArray[i].getTimestamp();
            if (lruTimestamp > currentTimestamp) {
                lruIndex = i;
                lruTimestamp = currentTimestamp;
            }
        }
        return lruIndex;
    }

    /**
     * MRU algorithm to evaluate index to be evicted.
     * 
     * @param startIndex Start index.
     * @param endIndex End index.
     * @return Index to be evicted.
     */
    private int mruReplacementAlgo(int startIndex, int endIndex) {
        int mruIndex = startIndex;
        long mruTimestamp = cacheArray[startIndex].getTimestamp();
        for (int i = startIndex; i <= endIndex ; i++) {
            long currentTimestamp = cacheArray[i].getTimestamp();
            if (mruTimestamp < currentTimestamp) {
                mruIndex = i;
                mruTimestamp = currentTimestamp;
            }
        }
        return mruIndex;
    }

    /**
     * CUSTOM replacement algorithm to evaluate index to be evicted.
     * Purpose of this method is to provide overriding to inject custom 
     * replacement algorithms. default is still pointing to LRU.
     * 
     * @param startIndex Start index
     * @param endIndex End index
     * @return Index to be evicted.
     */
    public int customReplacementAlgo(int startIndex, int endIndex) {
        return lruReplacementAlgo(startIndex, endIndex);
    }
    
    /**
     * Byte array to integer conversion.
     * 
     * @param b Byte array.
     * @return Integer value.
     */
    private static int byteArrayToInt(byte[] b) {
        final ByteBuffer bb = ByteBuffer.wrap(b);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();
    }
    
    /**
     * To obtain current timestamp in long format.
     * 
     * @return Current timestamp.
     */
    private long getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        return date.getTime();
    }

    /**
     * DUMMY call method to main memory for future usage.
     * 
     * @param key Key.
     * @return data retrieved from main memory.
     */
    private CacheEntry dummyCallMainMemory(Object key) {
        CacheEntry cacheEntry = new CacheEntry(getHash(key), null, false, getCurrentTime());
        return cacheEntry;
    }
}
