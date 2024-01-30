package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.IO.IO;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.vision.VisionSubsystem;

public class RobotContainer {


    public RobotContainer()
    {
        VisionSubsystem.getInstance();

        DriverStation.silenceJoystickConnectionWarning(true);

        IO.Initialize();

        DrivetrainSubsystem.getInstance().setDefaultCommand(new DriveCommand());

        IntakeSubsystem.getInstance().setDefaultCommand(new IntakeCommand());

    }

    public Command getAutonomousCommand()
    {
        return null;
    }
}
