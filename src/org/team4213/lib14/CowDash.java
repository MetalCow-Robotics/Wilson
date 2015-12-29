/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Hashtable;

/**
 * Convenience wrapper for SmartDashboard
 *
 * @author hughest1
 */
public class CowDash {
    protected static Hashtable values = new Hashtable();
    
    /**
     * Gets the value mapped to key from the dashboard.
     * If no value is mapped to this key, adds the fallback to the dashboard and returns this fallback.
     * @param key the key to look up
     * @param fallback the value to fall back to
     * @return the value mapped to key if it exists, otherwise fallback.
     */
    static public double getNumber(String key, double fallback) {
        try{
            return SmartDashboard.getNumber(key);
        }catch(Exception e) {
            CowDash.putNumber(key, fallback);
            return fallback;
        }
    }
    
    static public void putNumber(String key, double value) {
        values.put(key, Double.valueOf(value));
        SmartDashboard.putNumber(key, value);
    }
    
    
    /**
     * Gets the value mapped to key from the dashboard.
     * If no value is mapped to this key, adds the fallback to the dashboard and returns this fallback.
     * @param key the key to look up
     * @param fallback the value to fall back to
     * @return the value mapped to key if it exists, otherwise fallback.
     */
    static public boolean getBoolean(String key, boolean fallback) {
        try{
            return SmartDashboard.getBoolean(key);
        }catch(Exception e) {
            CowDash.putBoolean(key, fallback);
            return fallback;
        }
    }
    
    static public void putBoolean(String key, boolean value) {
        values.put(key, Boolean.valueOf(value));
        SmartDashboard.putBoolean(key, value);
    }
    
    /**
     * Gets the value mapped to key from the dashboard.
     * If no value is mapped to this key, adds the fallback to the dashboard and returns this fallback.
     * @param key the key to look up
     * @param fallback the value to fall back to
     * @return the value mapped to key if it exists, otherwise fallback.
     */
    static public String getString(String key, String fallback) {
        try{
            return SmartDashboard.getString(key);
        }catch(Exception e) {
            CowDash.putString(key, fallback);
            return fallback;
        }
    }
    
    static public void putString(String key, String value) {
        values.put(key, value);
        SmartDashboard.putString(key, value);
    }
}
