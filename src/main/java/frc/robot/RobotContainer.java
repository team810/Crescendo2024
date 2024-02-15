package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.IO.Controls;
import frc.robot.IO.IO;
import frc.robot.commands.ClimbCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.FireCommand;
import frc.robot.commands.Intake.IntakeFwdCommand;
import frc.robot.commands.Intake.IntakeRevCommand;
import frc.robot.commands.Intake.IntakeSourceCommand;
import frc.robot.commands.RevShooterTestCommand;
import frc.robot.subsystem.climber.ClimberSubsystem;
import frc.robot.subsystem.deflector.DeflectorSubsystem;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.shooter.ShooterSubsystem;
import frc.robot.util.Pneumatics;

public class RobotContainer {

    private final SendableChooser<Command> autoChooser;
    public RobotContainer() {

        DriverStation.silenceJoystickConnectionWarning(true);

        IO.Initialize();
        Pneumatics.getInstance();

//        VisionSubsystem.getInstance();
        ShooterSubsystem.getInstance();
        IntakeSubsystem.getInstance();
        ClimberSubsystem.getInstance();

        DrivetrainSubsystem.getInstance().setDefaultCommand(new DriveCommand());


        autoChooser = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto Chooser", autoChooser);

        buttonConfig();

    }
    void buttonConfig()
    {
        new Trigger(() -> IO.getButtonValue(Controls.intakeFWD).get()).whileTrue(new IntakeFwdCommand());
        new Trigger(() -> IO.getButtonValue(Controls.intakeREVS).get()).whileTrue(new IntakeRevCommand());
        new Trigger(() -> IO.getButtonValue(Controls.sourceIntake).get()).whileTrue(new IntakeSourceCommand());

        new Trigger(() -> IO.getButtonValue(Controls.fire).get()).whileTrue(new FireCommand());
        new Trigger(() -> IO.getButtonValue(Controls.rev).get()).whileTrue(new RevShooterTestCommand());

        new Trigger(() -> IO.getButtonValue(Controls.releaseClimber).get()).toggleOnTrue(new InstantCommand(() -> ClimberSubsystem.getInstance().releaseClimber()));
        new Trigger(() -> IO.getButtonValue(Controls.climb).get()).whileTrue(new ClimbCommand());

        new Trigger(() -> IO.getButtonValue(Controls.toggleDeflector).get()).onTrue(new InstantCommand(() -> DeflectorSubsystem.getInstance().toggleDeflectorState()));

    }
//    private Command getRevShooterCommand()
//    {
//        switch (((AlignmentRectangle)
//                AutoTurnConstants.RECTANGLE_SET.findRectangle(
//                        DrivetrainSubsystem.getInstance().getPose())).getType())
//        {
//            case redSpeaker, blueSpeaker -> {
//                return new RevSpeakerCommand();
//            }
//            case amp -> {
//                return new RevAmpCommand();
//            }
//            default -> {
//                return new InstantCommand(() -> {System.out.println("Not in Amp or Speaker Zone!!!");});
//            }
//        }
//    }
//    private Command getFireCommand()
//    {
//        switch (((AlignmentRectangle)
//                AutoTurnConstants.RECTANGLE_SET.findRectangle(
//                        DrivetrainSubsystem.getInstance().getPose())).getType())
//        {
//            case redSpeaker, blueSpeaker, amp -> {
//                return new FireCommand();
//            }
//            default -> {
//                return new InstantCommand(() -> {System.out.println("Not in amp or speaker zone");});
//            }
//        }
//    }
    public Command getAutonomousCommand()
    {
        DrivetrainSubsystem.getInstance().resetOdometry(new Pose2d(2,2, new Rotation2d()));
        return autoChooser.getSelected();
    }
}
