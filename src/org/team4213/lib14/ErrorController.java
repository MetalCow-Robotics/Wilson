/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

/**
 *
 * @author hughest1
 */
public abstract class ErrorController {
    public double target=0;
    public void setTarget(double currentTarget) {
        this.target = currentTarget;
    }
    abstract public double feedAndGetValue(double currentValue);
}
