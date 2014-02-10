/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nsetcache;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit testing class for ClientCache in-turn testing all classes beneath it.
 * 
 * @author panchal
 */
public class ClientCacheTest {

    /**
     * Test for basic read/write operation. Value should retrievable once 
     * written to cache.
     */
    @Test
    public void testBasicRW() {
        ClientCache cache = new ClientCache(8, 2, "LRU");
        cache.put("Lionel", "Messi");
        cache.put("Christiano", "Ronaldo");
        Assert.assertEquals(cache.get("Lionel"), "Messi");
        Assert.assertEquals(cache.get("Christiano"), "Ronaldo");
    }
    
    /**
     * To verify keys belongs to same set ends up in correct set as additional 
     * entry. verification is done by accessing array by index, instead of 
     * get() method.
     */
    @Test
    public void testEntryForSameSet() {
        ClientCache cache = new ClientCache(8, 2, "LRU");
        cache.put(16, "Christiano");
        cache.put(32, "Ronaldo");
        Assert.assertEquals(cache.cacheArray[0].getData(), "Christiano");
        Assert.assertEquals(cache.cacheArray[1].getData(), "Ronaldo");
    }
    
    /**
     * Test to verify consecutive write operation for same key with update 
     * content replaces same entry in a same set with new data.
     */
    @Test
    public void testIncrementalUpdateSameKey() {
        ClientCache cache = new ClientCache(8, 2, "LRU");
        cache.put(16, "Christiano");
        cache.put(16, "Ronaldo");
        Assert.assertEquals(cache.get(16), "Ronaldo");
    }
    
    /**
     * Testing LRU eviction algorithm, making sure, least recently used entry 
     * is evicted in case of fully occupied entries of a given set.
     * 
     * @throws InterruptedException 
     */
    @Test
    public void testLRU() throws InterruptedException {
        ClientCache cache = new ClientCache(8, 2, "LRU");
        cache.put(16, "Christiano");
        Thread.sleep(50);
        cache.put(32, "Messi");
        Thread.sleep(50);
        cache.put(48, "Ronaldo");
        Assert.assertEquals(cache.get(16), null);
        Assert.assertEquals(cache.get(32), "Messi");
        Assert.assertEquals(cache.get(48), "Ronaldo");
    }
    
    /**
     * Testing MRU eviction algorithm, making sure, most recently used entry 
     * is evicted in case of fully occupied entries of a given set.
     * 
     * @throws InterruptedException 
     */
    @Test
    public void testMRU() throws InterruptedException {
        ClientCache cache = new ClientCache(8, 2, "MRU");
        cache.put(16, "Christiano");
        Thread.sleep(50);
        cache.put(32, "Messi");
        Thread.sleep(50);
        cache.put(48, "Ronaldo");
        Assert.assertEquals(cache.get(16), "Christiano");
        Assert.assertEquals(cache.get(32), null);
        Assert.assertEquals(cache.get(48), "Ronaldo");
    }
    
    /**
     * Testing CUSTOM eviction algorithm, making sure, last index entry 
     * is evicted in case of fully occupied entries of a given set. 
     * 
     * @throws InterruptedException 
     */
    @Test
    public void testCUSTOM() throws InterruptedException {
        ClientCache cache = new ClientCache(8, 4, "CUSTOM");
        cache.put(16, "Christiano");
        Thread.sleep(50);
        cache.put(32, "Ronaldo");
        Thread.sleep(50);
        cache.put(48, "Lionel");
        Thread.sleep(50);
        cache.put(64, "Messi");
        Thread.sleep(50);
        cache.put(80, "Kaka");
        Assert.assertEquals(cache.get(16), "Christiano");
        Assert.assertEquals(cache.get(32), "Ronaldo");
        Assert.assertEquals(cache.get(48), "Lionel");
        Assert.assertEquals(cache.get(64), null);
        Assert.assertEquals(cache.get(80), "Kaka");
    }
    
    /**
     * Prints cache contents for visual checks.
     * 
     * @param cache Cache to be printed.
     */
    public void print(ClientCache cache) {
        System.out.print("[");
        for (CacheEntry cacheEntry : cache.cacheArray) {
            System.out.print(cacheEntry.getTag() + "," + cacheEntry.getData() + "," + cacheEntry.getTimestamp() + "\t");
        }
        System.out.println("]");
    }
}
