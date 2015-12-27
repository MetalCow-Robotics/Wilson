/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author Thaddeus Hughes
 */
public class CowVic extends Victor {
    private double exponentialScaling;
    private boolean invert;
    
    public CowVic(int port) {
        super(port);
        this.exponentialScaling=1;
        this.invert=false;
    }
    public CowVic(int port, boolean invert) {
        super(port);
        this.exponentialScaling=1;
        this.invert=invert;
    }
    public CowVic(int port, boolean invert, double exponentialScaling) {
        super(port);
        this.exponentialScaling=exponentialScaling;
        this.invert=invert;
    }
    
    public void set (double power) {
        super.set((invert ? -1:1)*CowMath.expScale(power, exponentialScaling));
    }
}
