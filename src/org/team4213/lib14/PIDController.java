/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    
    Timer timeSinceLastFeed;
    
    /**
     * Creates a new PID controller.
     * 
     * @param kp the proportional gain
     * @param ki the integral gain
     * @param maxIInfluence the maximum influence the integral is allowed to have on the response
     * @param kd the derivative gain
     */
    public PIDController(String prefix, double kp, double ki, double maxIInfluence, double kd) {
        super(prefix);
        this.kp=kp;
        this.ki=ki;
        this.maxIInfluence=maxIInfluence;
        this.kd=kd;
        this.timeSinceLastFeed = new Timer();
        resetTimer();
    }
    
    /**
     * Resets the integral response to zero.
     */
    public void resetIntegral() {
        integralResponse=0;
    }
    
    public void resetTimer() {
        timeSinceLastFeed.reset();
        timeSinceLastFeed.start();
    }
    
    // TODO: Add time as a factor into this
    // TODO: Use a list/time-limited integral
    public double feedAndGetValue(double currentValue) {
        double timeSpan = timeSinceLastFeed.get();
        resetTimer();
        
        kp=CowDash.getNumber(prefix+"_kp",kp);
        ki=CowDash.getNumber(prefix+"_ki",ki);
        kd=CowDash.getNumber(prefix+"_kd",kd);
        maxIInfluence=CowDash.getNumber(prefix+"_maxIInfluence",maxIInfluence);
        
        
        // Current error is target minus current value (duh)
        double thisError = target-currentValue;
        CowDash.putNumber(prefix+"_error",thisError);
        
        // Accumulate this error into the integral, weighted by the integral gain.
        integralResponse+=thisError*ki *timeSpan;
        // Limit the integral response magnitude to the maxIInfluence
        integralResponse = CowMath.limitToSignless(integralResponse, maxIInfluence);
        
        // Derivative is the difference between current and last errors (divided by time inbetween loops but this is rough)
        double derivative = (thisError-(target-lastValue)) /timeSpan;
        // Set the last value to the current one
        lastValue=currentValue;
        
        // Sum up terms and return them to give the response
        return thisError*kp + integralResponse + derivative*kd;
    }
}
