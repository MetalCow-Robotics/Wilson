

package org.team4213.wilson;

import com.kauailabs.nav6.frc.BufferingSerialPort;
import com.kauailabs.nav6.frc.IMU;
import edu.wpi.first.wpilibj.visa.VisaException;
import java.util.Hashtable;
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
        
        motors[1] = new CowVic(1,false,0.8);
        motors[2] = new CowVic(2,true,0.8);
        motors[3] = new CowVic(3,true,0.8);
    }
    
    public void rawDriveXYWT(double x, double y, double omega, double maxSpeed) {
        // TODO: Implement this
    }
}
