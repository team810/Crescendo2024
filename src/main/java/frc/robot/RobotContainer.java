package frc.robot;

import com.choreo.lib.Choreo;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.IO.Controls;
import frc.robot.IO.IO;
import frc.robot.commands.teleop.*;
import frc.robot.commands.teleop.intake.IntakeFwdCommand;
import frc.robot.commands.teleop.intake.IntakeRevCommand;
import frc.robot.commands.teleop.intake.IntakeSourceCommand;
import frc.robot.commands.teleop.score.AmpScoreCommand;
import frc.robot.commands.teleop.score.FireCommand;
import frc.robot.commands.teleop.score.RevSpeakerCommand;
import frc.robot.subsystem.climber.ClimberSubsystem;
import frc.robot.subsystem.deflector.DeflectorSubsystem;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.laser.LaserState;
import frc.robot.subsystem.laser.LaserSubsystem;
import frc.robot.subsystem.shooter.ShooterSubsystem;
import frc.robot.subsystem.tbone.TBoneSubsystem;

public class RobotContainer {

    public RobotContainer() {

        DriverStation.silenceJoystickConnectionWarning(true);

        IO.Initialize();

        ShooterSubsystem.getInstance();
        IntakeSubsystem.getInstance();
        ClimberSubsystem.getInstance();
        TBoneSubsystem.getInstance();
        LaserSubsystem.getInstance();

        DrivetrainSubsystem.getInstance().setDefaultCommand(new DriveCommand());

        Shuffleboard.getTab("Competition").addDouble("Match Time", DriverStation::getMatchTime);
        Shuffleboard.getTab("Competition").addBoolean("Game Piece Detected", () -> (LaserSubsystem.getInstance().getLaserState() == LaserState.Detected));

        buttonConfig();
    }

    void buttonConfig() {
        new Trigger(() -> IO.getButtonValue(Controls.intakeFWD).get()).whileTrue(new IntakeFwdCommand());
        new Trigger(() -> IO.getButtonValue(Controls.intakeREVS).get()).whileTrue(new IntakeRevCommand());
        new Trigger(() -> IO.getButtonValue(Controls.sourceIntake).get()).whileTrue(new IntakeSourceCommand());

        new Trigger(() -> IO.getButtonValue(Controls.fire).get()).whileTrue(new FireCommand());
//        new Trigger(() -> IO.getButtonValue(Controls.SpeakerScore).get()).whileTrue(new RevShooterTestCommand());

        new Trigger(() -> IO.getButtonValue(Controls.AmpScore).get()).whileTrue(new AmpScoreCommand());
        new Trigger(() -> IO.getButtonValue(Controls.revSpeaker).get()).whileTrue(new RevSpeakerCommand());
        new Trigger(() -> IO.getButtonValue(Controls.revTape).get()).whileTrue(new RevTapeCommand());

        new Trigger(() -> IO.getButtonValue(Controls.releaseClimber).get()).toggleOnTrue(new InstantCommand(() -> ClimberSubsystem.getInstance().releaseClimber()));
        new Trigger(() -> IO.getButtonValue(Controls.pinClimber).get()).toggleOnTrue(new InstantCommand(() -> ClimberSubsystem.getInstance().pinClimber()));
        new Trigger(() -> IO.getButtonValue(Controls.climb).get()).whileTrue(new ClimbCommand());
        new Trigger(() -> IO.getButtonValue(Controls.invertClimb).get()).whileTrue(new InvertClimbCommand());

        new Trigger(() -> IO.getButtonValue(Controls.toggleTBone).get()).toggleOnTrue(new TBoneCommand());
        new Trigger(() -> IO.getButtonValue(Controls.toggleDeflector).get()).onTrue(new InstantCommand(() -> DeflectorSubsystem.getInstance().toggleDeflectorState()));
    }


    public Command getAutonomousCommand() {
        return new SequentialCommandGroup(
                new WaitCommand(1),
                new FollowTrajectoryCommand(Choreo.getTrajectory("4 Peice Auto.1")),
                new WaitCommand(1),
                new FollowTrajectoryCommand(Choreo.getTrajectory("4 Peice Auto.2")),
                new WaitCommand(1),
                new FollowTrajectoryCommand(Choreo.getTrajectory("4 Peice Auto.3"))
        );
    }
}
