/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team4213.lib14;

import edu.wpi.first.wpilibj.RobotBase;
import java.util.Timer;
import edu.wpi.first.wpilibj.communication.FRCControl;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import java.util.TimerTask;
import java.util.Vector;

/**
 *
 * @author Thaddeus
 */
public class MCRIterativeRobot extends RobotBase {

    private boolean m_disabledInitialized;
    private boolean m_autonomousInitialized;
    private boolean m_teleopInitialized;
    private boolean m_testInitialized;
    boolean didDisabledPeriodic = false;
    boolean didAutonomousPeriodic = false;
    boolean didTeleopPeriodic = false;
    boolean didTestPeriodic = false;

    UpdaterTask manager = null;
    Timer managerUpdater = null;
    
    Vector loopables = new Vector();
    public void addLoopable(Loopable l) {
        loopables.addElement(l);
    }
    
    Vector loggables = new Vector();
    public void addLoggable(Loggable l) {
        loggables.addElement(l);
    }

    public void startCompetition() {
        LiveWindow.setEnabled(false);
        robotInit();
        manager = new UpdaterTask();
        managerUpdater = new Timer();
        managerUpdater.schedule(manager, 0L, (long) (10));
        
        
        
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void runLoopables(boolean isTeleop, boolean isAuton) {
        int i;
        for (i = 0; i < loopables.size(); ++i) {
          Loopable c = (Loopable) loopables.elementAt(i);
          if (c != null) {
              c.loop(isTeleop, isAuton);
          }
        }
    }

    private class UpdaterTask extends TimerTask {

        public void run() {
            try {
                if (isDisabled()) {
      // call DisabledInit() if we are now just entering disabled mode from
                    // either a different mode or from power-on
                    if (!m_disabledInitialized) {

                        disabledInit();
                        m_disabledInitialized = true;
                        // reset the initialization flags for the other modes
                        m_autonomousInitialized = false;
                        m_teleopInitialized = false;
                        m_testInitialized = false;
                    }
                    FRCControl.observeUserProgramDisabled();
                    disabledPeriodic();
                    runLoopables(false,false);
                    didDisabledPeriodic = true;
                } else if (isAutonomous()) {
      // call Autonomous_Init() if this is the first time
                    // we've entered autonomous_mode
                    if (!m_autonomousInitialized) {
        // KBS NOTE: old code reset all PWMs and relays to "safe values"
                        // whenever entering autonomous mode, before calling
                        // "Autonomous_Init()"
                        autonomousInit();
                        m_autonomousInitialized = true;
                        m_testInitialized = false;
                        m_teleopInitialized = false;
                        m_disabledInitialized = false;
                    }
                    getWatchdog().feed();
                    FRCControl.observeUserProgramAutonomous();
                    autonomousPeriodic();
                    runLoopables(false,true);
                    didAutonomousPeriodic = true;
                } else {
      // call Teleop_Init() if this is the first time
                    // we've entered teleop_mode
                    if (!m_teleopInitialized) {
                        LiveWindow.setEnabled(false);
                        teleopInit();
                        m_teleopInitialized = true;
                        m_testInitialized = false;
                        m_autonomousInitialized = false;
                        m_disabledInitialized = false;
                    }
                    getWatchdog().feed();
                    FRCControl.observeUserProgramTeleop();
                    teleopPeriodic();
                    runLoopables(true,false);
                    didTeleopPeriodic = true;
                }
                allPeriodic();
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    
    /*
     * All of these methods are meant to be overridden.
     */
    public void robotInit() {

    }

    public void disabledInit() {

    }

    public void autonomousInit() {

    }

    public void teleopInit() {

    }

    public void testInit() {

    }

    public void disabledPeriodic() {

    }

    public void autonomousPeriodic() {

    }

    public void teleopPeriodic() {

    }

    public void testPeriodic() {

    }

    public void allPeriodic() {

    }
}
