/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

import com.sun.squawk.util.MathUtils;
/**
 *
 * @author Thaddeus Hughes
 */
public class CowMath {
    public static double copySign(double from, double to) {
        if (from>0) return Math.abs(to);
        else if (from<0) return -Math.abs(to);
        else return 0;
    }
    
    public static double expScale(double in, double power) {
        return copySign(in, MathUtils.pow(Math.abs(in), power));
    }
    
    public static double limitToSignless(double in, double limit) {
        if (in>limit) return limit;
        else if (in<-limit) return -limit;
        else return in;
    }
    
    public static double[] normalize(double[] powers, double to) {
        double maxMag = to;
        for (int i=0; i<powers.length; i++) {
            if (Math.abs(powers[i]) > maxMag)
                maxMag = Math.abs(powers[i]);
        }
        if (maxMag <= to) return powers;
        for (int i=0; i<powers.length; i++) {
            powers[i] = powers[i] * to/maxMag;
        }
        return powers;
    }
    
    public static double[] normalize(double[] powers){
        return normalize(powers, 1);
    }
}
