package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
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
import frc.robot.commands.Intake.IntakeFwdCommand;
import frc.robot.commands.Intake.IntakeSourceCommand;
import frc.robot.commands.TBoneCommand;
import frc.robot.commands.score.AmpScoreCommand;
import frc.robot.commands.score.FireCommand;
import frc.robot.commands.score.SpeakerScoreCommand;
import frc.robot.subsystem.climber.ClimberSubsystem;
import frc.robot.subsystem.deflector.DeflectorSubsystem;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.shooter.ShooterSubsystem;

public class RobotContainer {

    private final SendableChooser<Command> autoChooser;
    public RobotContainer()
    {

        DriverStation.silenceJoystickConnectionWarning(true);

        IO.Initialize();

//        VisionSubsystem.getInstance();
        ShooterSubsystem.getInstance();
        IntakeSubsystem.getInstance();
        ClimberSubsystem.getInstance();
//        LaserSubsystem.getInstance();

        DrivetrainSubsystem.getInstance().setDefaultCommand(new DriveCommand());

        autoChooser = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto Chooser", autoChooser);

        buttonConfig();
    }

    void buttonConfig()
    {
        new Trigger(() -> IO.getButtonValue(Controls.intakeFWD).get()).whileTrue(new IntakeFwdCommand());
//        new Trigger(() -> IO.getButtonValue(Controls.intakeREVS).get()).whileTrue(new IntakeRevCommand());
        new Trigger(() -> IO.getButtonValue(Controls.sourceIntake).get()).whileTrue(new IntakeSourceCommand());

        new Trigger(() -> IO.getButtonValue(Controls.fire).get()).whileTrue(new FireCommand());
//        new Trigger(() -> IO.getButtonValue(Controls.SpeakerScore).get()).whileTrue(new RevShooterTestCommand());

        new Trigger(() -> IO.getButtonValue(Controls.AmpScore).get()).whileTrue(new AmpScoreCommand());
        new Trigger(() -> IO.getButtonValue(Controls.SpeakerScore).get()).whileTrue(new SpeakerScoreCommand());

        new Trigger(() -> IO.getButtonValue(Controls.releaseClimber).get()).toggleOnTrue(new InstantCommand(() -> ClimberSubsystem.getInstance().releaseClimber()));
        new Trigger(() -> IO.getButtonValue(Controls.pinClimber).get()).toggleOnTrue(new InstantCommand(() -> ClimberSubsystem.getInstance().pinClimber()));
        new Trigger(() -> IO.getButtonValue(Controls.climb).get()).whileTrue(new ClimbCommand());

        new Trigger(() -> IO.getButtonValue(Controls.toggleTBone).get()).toggleOnTrue(new TBoneCommand());
        new Trigger(() -> IO.getButtonValue(Controls.toggleDeflector).get()).onTrue(new InstantCommand(() -> DeflectorSubsystem.getInstance().toggleDeflectorState()));

//        new Trigger(() -> MathUtil.applyDeadband(IO.getJoystickValue(Controls.intakeManual).get(), .1) != 0).whileTrue(new IntakeManualCommand());
    }

    public Command getAutonomousCommand()
    {
//        DrivetrainSubsystem.getInstance().resetOdometry(new Pose2d(2,2, new Rotation2d()));
//        return autoChooser.getSelected();
        return null;
    }
}
