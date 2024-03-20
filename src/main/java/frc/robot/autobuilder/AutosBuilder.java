package frc.robot.autobuilder;

import com.choreo.lib.Choreo;
import com.choreo.lib.ChoreoTrajectory;
import edu.wpi.first.wpilibj2.command.*;
import frc.lib.MechanismState;
import frc.robot.commands.ChoreoTrajectoryCommand;
import frc.robot.commands.intake.IntakeFwdCommand;
import frc.robot.subsystem.deflector.DeflectorSubsystem;
import frc.robot.subsystem.drivetrain.DrivetrainMode;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystem.intake.IntakeStates;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.shooter.ShooterMode;
import frc.robot.subsystem.shooter.ShooterSubsystem;

import java.util.ArrayList;

public class AutosBuilder {
    private AutosBuilder() {

    }

    private static void start(ChoreoTrajectory trajectory)
    {
        DrivetrainSubsystem.getInstance().setMode(DrivetrainMode.stop);

        DrivetrainSubsystem.getInstance().resetOdometryAuto(trajectory.getInitialState().getPose());
        DrivetrainSubsystem.getInstance().setYaw(trajectory.getInitialPose().getRotation().getDegrees());
    }

    private static Command generateScoreTapeCommand()
    {
        return new SequentialCommandGroup(
                new InstantCommand(() -> DeflectorSubsystem.getInstance().setDeflectorState(MechanismState.stored)),
                new InstantCommand(() -> ShooterSubsystem.getInstance().setShooterMode(ShooterMode.Tape)),
                new WaitCommand(1),
                new InstantCommand(() -> IntakeSubsystem.getInstance().setState(IntakeStates.fire)),
                new WaitCommand(.5),
                new InstantCommand(() -> ShooterSubsystem.getInstance().setShooterMode(ShooterMode.off))
        );
    }

    private static Command generateScoreSubCommand()
    {
        return new SequentialCommandGroup(
                new InstantCommand(() -> DeflectorSubsystem.getInstance().setDeflectorState(MechanismState.deployed)),
                new InstantCommand(() -> ShooterSubsystem.getInstance().setShooterMode(ShooterMode.Subwoofer)),
                new WaitCommand(1),
                new InstantCommand(() -> IntakeSubsystem.getInstance().setState(IntakeStates.fire)),
                new WaitCommand(.5),
                new InstantCommand(() -> ShooterSubsystem.getInstance().setShooterMode(ShooterMode.off))
        );
    }

    private static Command generateIntakeWhileDriveCommand(ChoreoTrajectory trajectory)
    {
        return new ParallelCommandGroup(
                new IntakeFwdCommand(),
                new ChoreoTrajectoryCommand(trajectory)
        );
    }

    private static Command generateIntakeWhileDrivingWithoutLaser(ChoreoTrajectory trajectory)
    {
        return new ParallelCommandGroup(
                new InstantCommand(() -> IntakeSubsystem.getInstance().setState(IntakeStates.fwd)),
                new InstantCommand(() -> ShooterSubsystem.getInstance().setShooterMode(ShooterMode.Tape)),
                new InstantCommand(() -> DeflectorSubsystem.getInstance().setDeflectorState(MechanismState.stored)),
                new ChoreoTrajectoryCommand(trajectory)
        );
    }

    public static Command generateAutos(Paths path) {

        switch (path) {
            case FourPieceAuto -> {
                ArrayList<ChoreoTrajectory> trajectories = Choreo.getTrajectoryGroup("4Piece");

                return new SequentialCommandGroup(
                        new InstantCommand(() -> start(trajectories.get(0))),
                        generateScoreSubCommand(),
                        generateIntakeWhileDriveCommand(trajectories.get(0)),
                        generateScoreTapeCommand(),
                        generateIntakeWhileDriveCommand(trajectories.get(1)),
                        generateScoreTapeCommand(),
                        generateIntakeWhileDriveCommand(trajectories.get(2)),
                        generateScoreTapeCommand()
                );
            }
            case TwoPieceBottom -> {
                ArrayList<ChoreoTrajectory> trajectories = Choreo.getTrajectoryGroup("Bottom2Piece");
                return new SequentialCommandGroup(
                        new InstantCommand(() -> start(trajectories.get(0))),
                        generateScoreSubCommand(),
                        generateIntakeWhileDriveCommand(trajectories.get(0)),
                        generateScoreSubCommand(),
                        generateIntakeWhileDriveCommand(trajectories.get(1))


                );
            }
            case Testing ->
            {
                ArrayList<ChoreoTrajectory> trajectories = Choreo.getTrajectoryGroup("Side");
                return new SequentialCommandGroup(
                        new InstantCommand(() -> start(trajectories.get(0))),
                        generateScoreSubCommand(),
                        generateIntakeWhileDriveCommand(trajectories.get(0))
                );
            }
            case TwoPieceCenter ->
            {
                ArrayList<ChoreoTrajectory> trajectories = Choreo.getTrajectoryGroup("SubMid2Peice");
                return new SequentialCommandGroup(
                        new InstantCommand(() -> start(trajectories.get(0))),
                        generateScoreSubCommand(),
                        generateIntakeWhileDrivingWithoutLaser(trajectories.get(0))
                );
            }
            case Score ->
            {
                return generateScoreSubCommand();
            }
            case None -> {
                return new InstantCommand(() -> System.out.println("No auto selected"));
            }
        }
        return null;
    }



}
