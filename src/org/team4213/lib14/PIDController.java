/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

/**
 * An ErrorController implemented with a simple Proportional-(limited)Integral-Derivative controller
 * 
 * @author hughest1
 */
public class PIDController extends ErrorController {
    /**
     * Constants for the controller
     * @var kp the proportional gain
     * @var ki the integral gain
     * @var maxIInfluence the maximum influence the integral is allowed to have on the response
     * @var kd the derivative gain
     */
    public double kp, ki, maxIInfluence, kd;
    
    // The last value the system was at
    double lastValue=0;
    // The integral of errors
    double integralResponse=0;
    
    /**
     * Creates a new PID controller.
     * 
     * @param kp the proportional gain
     * @param ki the integral gain
     * @param maxIInfluence the maximum influence the integral is allowed to have on the response
     * @param kd the derivative gain
     */
    public PIDController(double kp, double ki, double maxIInfluence, double kd) {
        this.kp=kp;
        this.ki=ki;
        this.maxIInfluence=maxIInfluence;
        this.kd=kd;
    }
    
    /**
     * Resets the integral response to zero.
     */
    public void resetIntegral() {
        integralResponse=0;
    }
    
    // TODO: Add time as a factor into this
    // TODO: Use a list/time-limited integral
    public double feedAndGetValue(double currentValue) {
        // Current error is target minus current value (duh)
        double thisError = target-currentValue;
        
        // Accumulate this error into the integral, weighted by the integral gain.
        // Ideally we'd multiply by time inbetween loops but this is rough
        integralResponse+=thisError*ki;
        // Limit the integral response magnitude to the maxIInfluence
        integralResponse = CowMath.limitToSignless(integralResponse, maxIInfluence);
        
        // Derivative is the difference between current and last errors (divided by time inbetween loops but this is rough)
        double derivative = thisError-(target-lastValue);
        // Set the last value to the current one
        lastValue=currentValue;
        
        // Sum up terms and return them to give the response
        return thisError*kp + integralResponse + derivative*kd;
    }
}
