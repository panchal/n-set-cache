/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nsetcache;

/**
 * This class defines Single Cache Entry Structure.
 * 
 * @author panchal
 */
public class CacheEntry {
    
    public int tag;
    public Object data;
    public boolean isEmpty;
    public long timestamp;
    
    /**
     * Default constructor. Useful for populating an empty cache entry.
     * 
     */
    public CacheEntry() {
        this.tag = 0;
        this.data = "null";
        this.isEmpty = true;
        this.timestamp = 0;
    }
    
    /**
     * Constructor. Useful for populating entire structure at once.
     * 
     * @param tag Integer representation of hash key.
     * @param data Actual data.
     * @param isEmpty true, if empty.
     * @param timestamp recent access timestamp.
     */
    public CacheEntry(int tag, Object data, boolean isEmpty, long timestamp) {
        this.tag = tag;
        this.data = data;
        this.isEmpty = isEmpty;
        this.timestamp = timestamp;
    }

    /**
     * Get Integer representation of hash key.
     * 
     * @return Tag. Integer representation of hash key.
     */
    public int getTag() {
        return this.tag;
    }
    
    /**
     * Get actual data.
     * 
     * @return data. Actual data.
     */
    public Object getData() {
        return this.data;
    }
    
    /**
     * Check for emptiness.
     * 
     * @return true, if empty.
     */
    public boolean isEmpty() {
        return this.isEmpty;
    }
    
    /**
     * Get last accessed timestamp.
     * 
     * @return timestamp. Last accessed timestamp.
     */
    public long getTimestamp() {
        return this.timestamp;
    }
    
    /**
     * Set Integer representation of hash key.
     * 
     * @param tag Integer representation of hash key.
     */
    public void setTag(int tag) {
        this.tag = tag;
    }
    
    /**
     * Set actual data.
     * 
     * @param data Actual data.
     */
    public void setData(Object data) {
        this.data = data;
    }
    
    /**
     * Define for emptiness.
     * 
     * @param isEmpty true, if empty.
     */
    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
    
    /**
     * Set last accessed timestamp.
     * 
     * @param timestamp Last accessed timestamp.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
