package frc.robot.commands;

import com.choreo.lib.ChoreoTrajectory;
import com.choreo.lib.ChoreoTrajectoryState;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystem.drivetrain.DrivetrainMode;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;


public class ChoreoTrajectoryCommand extends Command {
    private final Timer timer;
    private final ChoreoTrajectory trajectory;
    private double previousVelocity = 0;

    public ChoreoTrajectoryCommand(ChoreoTrajectory trajectory)
    {
       timer = new Timer();
       this.trajectory = trajectory;
       addRequirements(DrivetrainSubsystem.getInstance());
    }
    @Override
    public void initialize() {
        DrivetrainSubsystem.getInstance().setMode(DrivetrainMode.teleop);
        timer.start();
    }

    @Override
    public void execute() {
       ChoreoTrajectoryState state = trajectory.sample(timer.get());
       DrivetrainSubsystem.getInstance().setMode(DrivetrainMode.trajectory);

       double velocity = Math.sqrt(
               Math.pow(state.velocityX,2) + Math.pow(state.velocityY, 2)
       );
       double acceleration = (velocity - previousVelocity)/ Robot.defaultPeriodSecs;

       Trajectory.State stateTrajectory = new Trajectory.State(
               timer.get(),
               velocity,
               acceleration,
               state.getPose(),
               state.heading
       );

       DrivetrainSubsystem.getInstance().setTrajectoryState(stateTrajectory);
       previousVelocity = velocity;

       DrivetrainSubsystem.getInstance().drive();
    }

   @Override
   public void end(boolean interrupted) {
       DrivetrainSubsystem.getInstance().setMode(DrivetrainMode.teleop);
       DrivetrainSubsystem.getInstance().setMode(DrivetrainMode.stop);
       DrivetrainSubsystem.getInstance().drive();
   }

    @Override
    public boolean isFinished() {
        return trajectory.getTotalTime() < timer.get() && DrivetrainSubsystem.getInstance().getDriveControllerAtSetpoint();
    }
}
