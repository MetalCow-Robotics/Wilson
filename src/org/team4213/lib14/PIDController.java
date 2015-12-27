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
public class PIDController extends ErrorController {
    public double kp, ki, maxIInfluence, kd;
    
    double lastValue=0;
    double integralResponse=0;
    
    public PIDController(double kp, double ki, double maxIInfluence, double kd) {
        this.kp=kp;
        this.ki=ki;
        this.maxIInfluence=maxIInfluence;
        this.kd=kd;
    }
    
    public void resetIntegral() {
        integralResponse=0;
    }
    
    public double feedAndGetValue(double currentValue) {
        double thisError = target-currentValue;
        
        integralResponse+=thisError*ki;
        integralResponse = CowMath.limitToSignless(integralResponse, maxIInfluence);
        
        double derivative = thisError-(target-lastValue);
        lastValue=currentValue;
        
        return thisError*kp + integralResponse + derivative*kd;
    }
}
