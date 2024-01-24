package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.IO.IO;
import frc.robot.commands.AlignCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystem.vision.VisionSubsystem;

public class RobotContainer {


    public RobotContainer()
    {
        VisionSubsystem.getInstance();

        DriverStation.silenceJoystickConnectionWarning(true);

        IO.Initialize();

        DrivetrainSubsystem.getInstance().setDefaultCommand(new DriveCommand());

//        VisionSubsystem.getInstance().setDefaultCommand(new AlignCommand());

    }

    public Command getAutonomousCommand()
    {
        return null;
    }
}
