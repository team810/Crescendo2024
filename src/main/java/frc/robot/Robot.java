package frc.robot;

import com.revrobotics.REVPhysicsSim;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

public class Robot extends LoggedRobot
{
    private Command autonomousCommand;
    
    private RobotContainer robotContainer;

    @Override
    public void robotInit()
    {
        Logger.recordMetadata("ProjectName", "Crescendo"); // Set a metadata value
        if (isReal()) {
            Logger.addDataReceiver(new NT4Publisher());
            Logger.addDataReceiver(new WPILOGWriter());
        } else {
            Logger.addDataReceiver(new NT4Publisher());
        }
        Logger.start();

        robotContainer = new RobotContainer();
        CameraServer.startAutomaticCapture();
    }

    @Override
    public void robotPeriodic()
    {
        if  (Robot.isSimulation())
        {
            REVPhysicsSim.getInstance().run();
        }
        CommandScheduler.getInstance().run();

        Logger.recordOutput("BatteryVoltage/", RobotController.getBatteryVoltage());
    }

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}
    
    @Override
    public void autonomousInit()
    {
        autonomousCommand = robotContainer.getAutonomousCommand();
        if (autonomousCommand != null)
        {
            autonomousCommand.schedule();
        }
    }
    
    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit()
    {
        if (autonomousCommand != null)
        {
            autonomousCommand.cancel();
        }
    }
    
    @Override
    public void teleopPeriodic() {}

    @Override
    public void testInit()
    {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {}

    @Override
    public void simulationInit() {}

    @Override
    public void simulationPeriodic() {}
}
