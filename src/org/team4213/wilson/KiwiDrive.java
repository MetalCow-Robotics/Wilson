

package org.team4213.wilson;

import com.kauailabs.nav6.frc.BufferingSerialPort;
import com.kauailabs.nav6.frc.IMU;
import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.visa.VisaException;
import java.util.Hashtable;
import org.team4213.lib14.AnglePIDController;
import org.team4213.lib14.CowDash;
import org.team4213.lib14.CowMath;
import org.team4213.lib14.CowVic;
import org.team4213.lib14.PIDController;

/**
 *
 * @author hughest1
 * 
 * This class is for the drivetrain of Wilson, which is a Kiwi Drivetrain
 */
public class KiwiDrive {
    public String name = "KiwiDrive";
    
    // Stuff for the nav6 gyroscope/imu
    BufferingSerialPort imuSerialPort = null;
    IMU imu = null;
    
    // Drive motors
    CowVic[] motors = new CowVic[3];
    
    
    boolean regulateHeading = false;
    boolean fieldOriented = false;
    boolean haloDrive = false;
    
    public void toggleRegulateHeading(){
        if(regulateHeading) {
            regulateHeading = fieldOriented = haloDrive = false;
        } else{
            regulateHeading=true;
            if (imuAvailable())
                imu.zeroYaw();
        }
    }
    public void toggleFieldOriented() {
        if(fieldOriented) {
            fieldOriented=false;
        } else {
            if (!regulateHeading) {
                regulateHeading=true;
                if (imuAvailable())
                    imu.zeroYaw();
            }
            fieldOriented=true;
            haloDrive=false;
        }
    }
    public void toggleHaloDrive() {
        if(haloDrive) {
            haloDrive=false;
        }else {
            if (!regulateHeading) {
                regulateHeading=true;
                if (imuAvailable())
                    imu.zeroYaw();
            }
            haloDrive=true;
            fieldOriented=false;
        }
    }
    
    public AnglePIDController headingController;
            
    public KiwiDrive() {
        // This sets up the nav6 gyroscope
        try{
            imuSerialPort = new BufferingSerialPort (57600);
            imu = new IMU(imuSerialPort, (byte)75);
        }catch(VisaException e){
            e.printStackTrace();
            imuSerialPort = null;
            imu = null;
        }
        
        motors[0] = new CowVic(1,true,1);
        motors[1] = new CowVic(2,false,1);
        motors[2] = new CowVic(3,true,1);
        
        headingController = new AnglePIDController(name+"::headingController",1000.0/180.0,0,0, 0.2);
        headingController.addTarget("north", 0);
        headingController.addTarget("east", 90);
        headingController.addTarget("south", 180);
        headingController.addTarget("west", 270);
    }
    
    public void setHeading(String name) {
        if (name!=null)
            headingController.setTarget(name);
    }
    
    public boolean imuAvailable() {
        return imu!=null && imuSerialPort!=null;
    }
    
    public double getCurrentHeading() {
        if (this.imuAvailable())
            return imu.getYaw();
        else return 0;
    }
    
    public void rawDriveXYWT(double x, double y, double omega, double maxSpeed) {
        // Get scaling factor from dashboard and apply it to all motors
        for (int m=0;m<motors.length;m++)
            motors[m].exponentialScaling =CowDash.getNumber(name+"::exponentialScaling",1);
        
        // Compute drive speed for each wheel
        double[] powers = CowMath.normalize(
            new double[] {
                // The only reason omega is multiplied by 0.5 is to tone down how fast turns occur.
                (0.5*x + 0.866*y + omega*0.5),
                (-0.5*x + 0.866*y -omega*0.5),
                (- x + omega*0.5)
            }
        ,maxSpeed);
        
        // Send out motor commands
        for(int i=0;i<powers.length;i++)
            motors[i].set(powers[i]);
    }
    
    public void driveXYW(double x, double y, double omega, double maxSpeed) {
        // Log out current data about drivetrain
        CowDash.putBoolean(name+"::imuAvailable()", imuAvailable());
        CowDash.putBoolean(name+"::regulateHeading", regulateHeading);
        CowDash.putBoolean(name+"::haloDrive", haloDrive);
        CowDash.putBoolean(name+"::fieldOriented", fieldOriented);
        
        double orientationTheta = getCurrentHeading();
        CowDash.putNumber(name+"::orientationTheta", orientationTheta);
        
        // If we need to regulate heading and can do it
        if (imuAvailable() && regulateHeading) {
            /**
             * Halo Drive Mode:
             * 
             * - Robot moves in x-y space relative to field
             * - Robot automatically turns to face direction of travel in x-y space
             */
            if (haloDrive) {
                // Compute the angle of the stick and convert to degrees
                double commandTheta = MathUtils.atan2(x,y) *180/Math.PI;

                // Compute the difference between field oriented theta and robot's current theta. This represents the equivalent angle the user is pushing the drive stick at.
                double correctedTheta = commandTheta - orientationTheta;

                // Compute the distance in x-y space the stick is pushed away (pythagorean theorem)
                double magnitude = Math.sqrt(y*y+x*x);
                // If the stick is pushed far enough, set the target angle to the direction the commanded.
                if (magnitude>0.2)
                    headingController.setTarget(commandTheta);
                // Constrain magnitude to 1
                if (magnitude>1) magnitude=1;

                // Convert back from polar coords to cartesian. Remember to convert to radians.
                x = magnitude*Math.sin(correctedTheta*Math.PI/180);
                y = magnitude*Math.cos(correctedTheta*Math.PI/180);


                // Send out drive commands
                rawDriveXYWT(x,y,headingController.feedAndGetValue(orientationTheta), maxSpeed);
            } else {
                /**
                    * Field oriented drive mode:
                    * 
                    * - Robot moves in x-y space relative to field
                    * - I.E. a push forwards results in the robot moving "north" on the field, regardless of orientation.
                    */
                if (fieldOriented) {
                    // This code here is just like that up above.
                    double commandTheta = MathUtils.atan2(x,y)*180/Math.PI;

                    double correctedTheta = commandTheta - orientationTheta;
                    double magnitude = Math.sqrt(y*y+x*x);
                    if (magnitude>1) magnitude=1;

                    x = magnitude*Math.sin(correctedTheta*Math.PI/180);
                    y = magnitude*Math.cos(correctedTheta*Math.PI/180);
                }

                // If there's a significant rotation request, just act like rawDriveXYWT. However, set the target to the current heading so it behaves like normal.
                if (Math.abs(omega)>0.1) {
                    rawDriveXYWT(x,y,omega,maxSpeed);
                    headingController.setTarget(imu.getYaw());
                    headingController.reset();
                // If there's no request to rotate, regulate rotation.
                } else {
                    rawDriveXYWT(x,y,headingController.feedAndGetValue(orientationTheta), maxSpeed);
                }
            }
            //Short circuit out; we've accomplished setting motor values!
            return;
        }
        
        // Not in regulating mode? Okay, just act like rawDriveXYWT.
        rawDriveXYWT(x,y,omega,maxSpeed);
    }
}
