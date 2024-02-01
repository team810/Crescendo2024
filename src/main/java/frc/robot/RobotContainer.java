package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.IO.IO;
import frc.robot.commands.ClimberCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystem.climber.ClimberSubsystem;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.shooter.ShooterSubsystem;
import frc.robot.subsystem.vision.VisionSubsystem;

public class RobotContainer {


    public RobotContainer()
    {

        DriverStation.silenceJoystickConnectionWarning(true);

        IO.Initialize();

        VisionSubsystem.getInstance();
        ShooterSubsystem.getInstance();
        DrivetrainSubsystem.getInstance().setDefaultCommand(new DriveCommand());
        IntakeSubsystem.getInstance().setDefaultCommand(new IntakeCommand());
        ClimberSubsystem.getInstance().setDefaultCommand(new ClimberCommand());
    }

    public Command getAutonomousCommand()
    {
        return null;
    }
}
