/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

import java.util.Hashtable;

/**
 *
 * @author hughest1
 */
public abstract class ErrorController {
    public double target;
    Hashtable targetsMap;

    public ErrorController() {
        this.targetsMap = new Hashtable();
        this.target=0;
    }
    public void addTarget(String targetKey, double targetValue) {
        targetsMap.put(targetKey, new Double(targetValue));
    }
    
    public void setTarget(double currentTarget) {
        this.target = currentTarget;
    }
    
    abstract public double feedAndGetValue(double currentValue);
    
    public void setTarget(String targetKey) {
        Double res = (Double) targetsMap.get(targetKey);
        if (res!=null) target = res.doubleValue();
    }
}
