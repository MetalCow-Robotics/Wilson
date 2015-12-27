

package org.team4213.wilson;

import com.kauailabs.nav6.frc.BufferingSerialPort;
import com.kauailabs.nav6.frc.IMU;
import edu.wpi.first.wpilibj.visa.VisaException;

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
    
    public KiwiDrive() {
        // This sets up the nav6 gyroscope
        try{
            imuSerialPort = new BufferingSerialPort (57600);
            imu = new IMU(imuSerialPort, (byte)75);
        }catch(VisaException e){
            imuSerialPort = null;
            imu = null;
        }
    }
}
