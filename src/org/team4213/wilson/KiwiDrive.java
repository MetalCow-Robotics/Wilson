

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
        
        motors[0] = new CowVic(1,true,0.6);
        motors[1] = new CowVic(2,false,0.6);
        motors[2] = new CowVic(3,true,0.6);
        
        headingController = new AnglePIDController(name+"::headingController",1.0/90.0,0.1,1,0);
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
         
        double[] powers = CowMath.normalize(
            new double[] {
                0.5*x + 0.866*y + omega,
                -0.5*x + 0.866*y -omega,
                - x + omega
            }
        ,maxSpeed);
        
        for(int i=0;i<powers.length;i++)
            motors[i].set(powers[i]);
    }
    
    public void driveXYW(double x, double y, double omega, double maxSpeed) {
        CowDash.putBoolean(name+"::imuAvailable()", imuAvailable());
        CowDash.putBoolean(name+"::regulateHeading", regulateHeading);
        CowDash.putBoolean(name+"::haloDrive", haloDrive);
        CowDash.putBoolean(name+"::fieldOriented", fieldOriented);
        
        if (imuAvailable() && regulateHeading) {
            if (haloDrive) {
                double commandTheta = MathUtils.atan2(x,y)*180/Math.PI;
                headingController.setTarget(commandTheta);
                double orientationTheta = imu.getYaw();
                double correctedTheta = commandTheta - orientationTheta;
                double magnitude = Math.sqrt(y*y+x*x);

                x = magnitude*Math.sin(correctedTheta);
                y = magnitude*Math.cos(correctedTheta);
                
                rawDriveXYWT(x,y,headingController.feedAndGetValue(imu.getYaw()), maxSpeed);
            } else {
                if (fieldOriented) {
                    double commandTheta = MathUtils.atan2(x,y)*180/Math.PI;
                    double orientationTheta = imu.getYaw();
                    double correctedTheta = commandTheta - orientationTheta;
                    double magnitude = Math.sqrt(y*y+x*x);

                    x = magnitude*Math.sin(correctedTheta);
                    y = magnitude*Math.cos(correctedTheta);
                }
                
                if (Math.abs(omega)>0.1) {
                    rawDriveXYWT(x,y,omega,maxSpeed);
                    headingController.setTarget(imu.getYaw());
                    headingController.resetIntegral();
                } else {
                    rawDriveXYWT(x,y,headingController.feedAndGetValue(imu.getYaw()), maxSpeed);
                }
            }
        } else {
            rawDriveXYWT(x,y,omega,maxSpeed);
        }
    }
}
