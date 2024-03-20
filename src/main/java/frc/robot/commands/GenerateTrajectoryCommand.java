package frc.robot.commands;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.drivetrain.DrivetrainMode;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;


public class GenerateTrajectoryCommand extends Command {
    private final Timer timer;
    private final Trajectory trajectory;
    private Trajectory.State currentState;

    public GenerateTrajectoryCommand(Trajectory trajectory) {
        timer = new Timer();
        this.trajectory = trajectory;
        currentState = this.trajectory.sample(0);

        addRequirements(DrivetrainSubsystem.getInstance());
    }

    @Override
    public void initialize() {
        DrivetrainSubsystem.getInstance().setMode(DrivetrainMode.trajectory);
        timer.stop();
    }

    @Override
    public void execute() {
        currentState = trajectory.sample(timer.get());
        DrivetrainSubsystem.getInstance().setTrajectoryState(currentState);
    }

    @Override
    public boolean isFinished() {
        return timer.get() > trajectory.getTotalTimeSeconds() && DrivetrainSubsystem.getInstance().getDriveControllerAtSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        DrivetrainSubsystem.getInstance().setMode(DrivetrainMode.teleop);
    }
}
