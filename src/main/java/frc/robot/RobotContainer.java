package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.MathUtil;
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
import frc.robot.commands.*;
import frc.robot.subsystem.climber.ClimberSubsystem;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.util.AutoTurn.AutoTurnConstants;
import frc.robot.util.Rectangles.AlignmentRectangle;

public class RobotContainer {

    private final SendableChooser<Command> autoChooser;
    public RobotContainer() {

        DriverStation.silenceJoystickConnectionWarning(true);

        IO.Initialize();

//        VisionSubsystem.getInstance();
//        ShooterSubsystem.getInstance();
        IntakeSubsystem.getInstance();
//
        DrivetrainSubsystem.getInstance().setDefaultCommand(new DriveCommand());
//        ClimberSubsystem.getInstance().setDefaultCommand(new ClimberCommand());


        autoChooser = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto Chooser", autoChooser);

        buttonConfig();
    }
    void buttonConfig()
    {
        new Trigger(() -> IO.getButtonValue(Controls.intakeFwd).get()).
                onTrue(new IntakeGroundCommand());

        new Trigger(() -> MathUtil.applyDeadband(IO.getJoystickValue(Controls.intakeManual).get(),.1) != 0).
                whileTrue(new IntakeManualCommand(IO.getJoystickValue(Controls.intakeManual)));

        new Trigger(() -> IO.getButtonValue(Controls.intakeSource).get()).
                whileTrue(new IntakeSourceCommand());

//        new Trigger(() -> IO.getButtonValue(Controls.revShooter).get()).
//                whileTrue(getRevShooterCommand());
        new Trigger(() -> IO.getButtonValue(Controls.revShooter).get()).
                whileTrue(new RevSpeakerCommand());

        new Trigger(() -> IO.getButtonValue(Controls.fire).get()).
                whileTrue(getFireCommand());

        new Trigger(() -> IO.getButtonValue(Controls.releaseClimber).get()).
                whileTrue(new InstantCommand(() -> ClimberSubsystem.getInstance().releaseClimber()));

//        new Trigger(() -> IO.getButtonValue(Controls.toggleDeflector).get()).toggleOnTrue(new InstantCommand(() -> ShooterSubsystem.getInstance().setDeflectorState(MechanismState.deployed)));
    }
    private Command getRevShooterCommand()
    {
        switch (((AlignmentRectangle)
                AutoTurnConstants.RECTANGLE_SET.findRectangle(
                        DrivetrainSubsystem.getInstance().getPose())).getType())
        {
            case redSpeaker, blueSpeaker -> {
                return new RevSpeakerCommand();
            }
            case amp -> {
                return new RevAmpCommand();
            }
            default -> {
                return new InstantCommand(() -> {System.out.println("Not in Amp or Speaker Zone!!!");});
            }
        }
    }
    private Command getFireCommand()
    {
        switch (((AlignmentRectangle)
                AutoTurnConstants.RECTANGLE_SET.findRectangle(
                        DrivetrainSubsystem.getInstance().getPose())).getType())
        {
            case redSpeaker, blueSpeaker, amp -> {
                return new FireCommand();
            }
            default -> {
                return new InstantCommand(() -> {System.out.println("Not in amp or speaker zone");});
            }
        }
    }
    public Command getAutonomousCommand()
    {
        DrivetrainSubsystem.getInstance().resetOdometry(new Pose2d(2,2, new Rotation2d()));
        return autoChooser.getSelected();
    }
}
