/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nsetcache;

/**
 * Abstraction of N-way set associative cache implementation.
 * Public Interface.
 *
 * @author panchal
 */
public interface Cache {

    /**
     * This method is used to retrieve data related to given key.
     * 
     * @param key Key to be retrieved.
     * @return Actual data.
     */
    public Object get(final Object key);
    
    /**
     * This method allows data to be written associated with given key.
     * 
     * @param key Key associated with given data.
     * @param value Actual data to be written.
     */
    public void  put(final Object key, final Object value);
    
    /**
     * Cache size.
     * 
     * @return Size of whole cache.
     */
    public int size();
    
    /**
     * Clears/Resets the cache.
     * 
     */
    public void clear();
    
    
}
