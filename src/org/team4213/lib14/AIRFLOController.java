/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

/**
 * Wrapper/convenience class for the AIRFLO gamepads that MetalCow has
 * 
 * @author hughest1
 */
import edu.wpi.first.wpilibj.Joystick;
import java.lang.Math;
import com.sun.squawk.util.MathUtils;

public class AIRFLOController extends Joystick {
	boolean[] previousStates;
	boolean[] toggleStates;
	
	public AIRFLOController (int port) {
		super(port);
		previousStates = new boolean[20];
		toggleStates = new boolean[20];
		for (short i=0;i<20;i++){
			previousStates[i] = false;
			toggleStates[i] = false;
		}
	}
	
	public double getLY(){
		return getRawAxis(2);
	}
	
	public double getLX(){
		return getRawAxis(1);
	}
	
	public double getRY() {
		return -getRawAxis(3);
	}
	public double getRX(){
		return getRawAxis(4);
	}
	
	public boolean getHeadingPadPressed(){
		return getRawButton(1) || getRawButton(2) || getRawButton(3) || getRawButton(4);
	}
	
	public double getHeadingPadDirection(){
		float x=0, y=0;
		if (getRawButton(1)) y-=1;
		if (getRawButton(2)) x+=1;
		if (getRawButton(3)) x-=1;
		if (getRawButton(4)) y+=1;
		return Math.toDegrees(MathUtils.atan2(x, y));
	}
	
	public boolean getButton(int n) {
		//previousStates[n] = getRawButton(n);
		//return previousStates[n];
		return getRawButton(n);
	}
	
	public boolean getButtonTripped(int n) {
		if (getRawButton(n)) {
			if (previousStates[n]){
				previousStates[n] = true;
				return false;
			} else {
				previousStates[n] = true;
				return true;
			}
			
		} else {
			previousStates[n] = false;
			return false;
		}
	}
	
	public boolean getButtonReleased(int n) {
		if (!getRawButton(n)) {
			if (previousStates[n]){
				previousStates[n] = false;
				return true;
			} else {
				previousStates[n] = false;
				return false;
			}
			
		} else {
			previousStates[n] = true;
			return false;
		}
	}
	
	public boolean getButtonToggled(int n) {
		if (!getRawButton(n)) {
			previousStates[n] = false;
		}else if(previousStates[n]){
			previousStates[n] = true;
		}else{
			previousStates[n] = true;
			toggleStates[n] = !toggleStates[n];
		}
		return toggleStates[n];
	}
}