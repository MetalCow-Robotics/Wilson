/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Convenience wrapper for SmartDashboard
 *
 * @author hughest1
 */
public class CowDash {
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
            SmartDashboard.putNumber(key, fallback);
            return fallback;
        }
    }
    
    static public void putNumber(String key, double value) {
        SmartDashboard.putNumber(key, value);
    }
}
