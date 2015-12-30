

package org.team4213.wilson;

import com.kauailabs.nav6.frc.BufferingSerialPort;
import com.kauailabs.nav6.frc.IMU;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.visa.VisaException;
import java.util.Hashtable;
import org.team4213.lib14.CowMath;
import org.team4213.lib14.CowVic;

/**
 *
 * @author hughest1
 * 
 * This class is for the drivetrain of Wilson, which is a Kiwi Drivetrain
 */
public class KiwiDrive {
    
    // Stuff for the nav6 gyroscope/imu
    BufferingSerialPort imuSerialPort = null;
    IMU imu = null;
    double integral = 0;
    double lastError = 0;
    
    // Drive motors
    CowVic[] motors = new CowVic[3];
    
    public KiwiDrive() {
        // This sets up the nav6 gyroscope
        try{
            imuSerialPort = new BufferingSerialPort (57600);
            imu = new IMU(imuSerialPort, (byte)75);
        }catch(VisaException e){
            imuSerialPort = null;
            imu = null;
        }
        
        motors[0] = new CowVic(1,false,0.8);
        motors[1] = new CowVic(2,true,0.8);
        motors[2] = new CowVic(3,false,0.8);
    }
    
    public void rawDriveXYWT(double x, double y, double omega, double maxSpeed) {
        
        motors[0].set((-0.5*x+0.866*y-omega)*maxSpeed);
        motors[1].set((0.5*x+0.866*y+omega)*maxSpeed);
        motors[2].set((x-omega)*maxSpeed);
    }
    
    public void drive(double x, double y, double omega, double maxSpeed) {
        double derivative = 0;
        double targetAngle = 0;
        double currentAngle = imu.getYaw();
        double error = targetAngle - currentAngle; 
        integral += error;
        integral = CowMath.limitToSignless(integral, 10);
        derivative = error - lastError;
        
        rawDriveXYWT(x,y, error*SmartDashboard.getNumber("kp")/1000 + integral * SmartDashboard.getNumber("ki")/1000 + derivative * SmartDashboard.getNumber("kd")/1000, maxSpeed);
        lastError = error;
    }
}
