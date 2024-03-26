package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.IO.Controls;
import frc.robot.IO.IO;
import frc.robot.autobuilder.AutosBuilder;
import frc.robot.autobuilder.Paths;
import frc.robot.commands.*;
import frc.robot.commands.intake.IntakeFwdCommand;
import frc.robot.commands.intake.IntakeRevCommand;
import frc.robot.commands.intake.IntakeSourceCommand;
import frc.robot.commands.score.AmpScoreCommand;
import frc.robot.commands.score.FireCommand;
import frc.robot.commands.score.RevSpeakerCommand;
import frc.robot.subsystem.climber.ClimberSubsystem;
import frc.robot.subsystem.deflector.DeflectorSubsystem;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.laser.LaserState;
import frc.robot.subsystem.laser.LaserSubsystem;
import frc.robot.subsystem.shooter.ShooterSubsystem;
import frc.robot.subsystem.tbone.TBoneSubsystem;
import frc.robot.subsystem.vision.VisionSubsystem;

public class RobotContainer {
    private final ShuffleboardTab competitionTab;
    private final SendableChooser<Paths> autoChooser = new SendableChooser<>();

    public RobotContainer() {

        DriverStation.silenceJoystickConnectionWarning(true);

        IO.Initialize();
        
        VisionSubsystem.getInstance();
        ShooterSubsystem.getInstance();
        IntakeSubsystem.getInstance();
        ClimberSubsystem.getInstance();
        TBoneSubsystem.getInstance();
        LaserSubsystem.getInstance();

        DrivetrainSubsystem.getInstance().setDefaultCommand(new DriveCommand());
        competitionTab = Shuffleboard.getTab("Competition");

        competitionTab.addDouble("Match Time", DriverStation::getMatchTime);
        competitionTab.addBoolean("Game Piece Detected", () -> (LaserSubsystem.getInstance().getLaserState() == LaserState.Detected));

        autoChooser.addOption("4 Piece Autos", Paths.FourPieceAuto);
        autoChooser.addOption("Score", Paths.Score);
        autoChooser.addOption("2 Piece Bottom", Paths.TwoPieceBottom);
        autoChooser.addOption("Side", Paths.Side);
        autoChooser.addOption("Sub Middle", Paths.TwoPieceCenter);
        autoChooser.addOption("Deliver", Paths.Deliver);
        autoChooser.setDefaultOption("None", Paths.None);

        competitionTab.add("Auto Chooser", autoChooser);

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
        new Trigger(() -> IO.getButtonValue(Controls.invertClimb).get()).whileTrue(new ReverseClimbCommand());

        new Trigger(() -> IO.getButtonValue(Controls.toggleTBone).get()).toggleOnTrue(new TBoneCommand());
        new Trigger(() -> IO.getButtonValue(Controls.toggleDeflector).get()).onTrue(new InstantCommand(() -> DeflectorSubsystem.getInstance().toggleDeflectorState()));
    }


    public Command getAutonomousCommand() {
        return AutosBuilder.generateAutos(autoChooser.getSelected());
    }
}
