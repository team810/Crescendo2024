package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.shooter.ShooterState;
import frc.robot.subsystem.shooter.ShooterSubsystem;


public class RevSpeakerCommand extends Command {

    public RevSpeakerCommand() {
        addRequirements(ShooterSubsystem.getInstance());
    }

    @Override
    public void execute() {
        ShooterSubsystem.getInstance().setShooterState(ShooterState.Speaker);
    }

    public void end(boolean interrupted) {
        ShooterSubsystem.getInstance().setShooterState(ShooterState.off);
    }
}
