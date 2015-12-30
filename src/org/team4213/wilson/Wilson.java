package org.team4213.wilson;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team4213.lib14.AIRFLOController;

/**
 * This is the entry point!
 * 
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation.
 */
public class Wilson extends IterativeRobot {
KiwiDrive Drive = new KiwiDrive();
AIRFLOController user = new AIRFLOController(1);

    /*
     * This function is run once when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        SmartDashboard.putNumber("kp", 1.0/180.0*1000);
        SmartDashboard.putNumber("ki", 1.0/180.0);
        SmartDashboard.putNumber("kd", 1.0/180.0);
        System.out.println("===================");
        System.out.println("     robotInit");
        System.out.println("===================");
    }
    
    /**
     * This function is called once when autonomous starts.
     * It is blocking.
     */
    public void autonomousInit() {
            System.out.println("autonomousInit");
    }

    /**
     * This function is called periodically (at about 50Hz) during autonomous.
     * It is blocking.
     */
    public void autonomousPeriodic() {
    System.out.println("autonomousPeriodic");
    }
    
    /**
     * This function is called once when teleop starts.
     * It is blocking.
     * 
     * This should be used for updating what each subsystem should do.
     */
    public void teleopInit() {
    System.out.println("teleopInit");        
    }

    /**
     * This function is called periodically (at about 50Hz) during teleop.
     * It is blocking.
     */
    public void teleopPeriodic() {
       Drive.drive(user.getLX(), user.getLY(), user.getRX(), user.getButton(8) ? 1 : 0.35);
    }
    
}
