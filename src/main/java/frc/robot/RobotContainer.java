package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.IO.Controls;
import frc.robot.IO.IO;
import frc.robot.commands.auto.intake.AutoIntakeIn;
import frc.robot.commands.auto.intake.AutoIntakeRev;
import frc.robot.commands.auto.intake.AutoIntakeStop;
import frc.robot.commands.auto.score.AutoShooterFire;
import frc.robot.commands.auto.score.AutoShooterStop;
import frc.robot.commands.auto.score.AutoSubwooferShoot;
import frc.robot.commands.teleop.ClimbCommand;
import frc.robot.commands.teleop.DriveCommand;
import frc.robot.commands.teleop.intake.IntakeFwdCommand;
import frc.robot.commands.teleop.intake.IntakeSourceCommand;
import frc.robot.commands.teleop.TBoneCommand;
import frc.robot.commands.teleop.score.AmpScoreCommand;
import frc.robot.commands.teleop.score.FireCommand;
import frc.robot.commands.teleop.score.SpeakerScoreCommand;
import frc.robot.subsystem.climber.ClimberSubsystem;
import frc.robot.subsystem.deflector.DeflectorSubsystem;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.shooter.ShooterSubsystem;
import frc.robot.subsystem.vision.VisionSubsystem;

public class RobotContainer {

    private final SendableChooser<Command> autoChooser;

    public RobotContainer() {

        DriverStation.silenceJoystickConnectionWarning(true);

        IO.Initialize();

        VisionSubsystem.getInstance();
        ShooterSubsystem.getInstance();
        IntakeSubsystem.getInstance();
        ClimberSubsystem.getInstance();
//        LaserSubsystem.getInstance();

        DrivetrainSubsystem.getInstance().setDefaultCommand(new DriveCommand());

        registerCommands();

        autoChooser = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto Chooser", autoChooser);

        buttonConfig();
    }

    void buttonConfig() {
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

    public void registerCommands() {
        NamedCommands.registerCommand("Intake In", new AutoIntakeIn());
        NamedCommands.registerCommand("Intake Stop", new AutoIntakeStop());
        NamedCommands.registerCommand("Intake Out", new AutoIntakeRev());
        NamedCommands.registerCommand("Subwoofer Shot", new AutoSubwooferShoot());
        NamedCommands.registerCommand("Shooter Stop", new AutoShooterStop());
        NamedCommands.registerCommand("Shooter Fire", new AutoShooterFire());
        NamedCommands.registerCommand("Full Score",
                new SequentialCommandGroup(
                        new AutoSubwooferShoot(),
                        new WaitCommand(1.5),
                        new AutoShooterFire(),
                        new WaitCommand(0.25),
                        new AutoShooterStop()
                ));
    }

    public Command getAutonomousCommand() {
//        DrivetrainSubsystem.getInstance().resetOdometry(new Pose2d(2,2, new Rotation2d()));
//        return autoChooser.getSelected();
        return AutoBuilder.buildAuto("Center");
    }
}
