package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.shooter.ShooterState;
import frc.robot.subsystem.shooter.ShooterSubsystem;


public class RevAmpCommand extends Command {

    public RevAmpCommand() {
        // each subsystem used by the command must be passed into the
        // addRequirements() method (which takes a vararg of Subsystem)
        addRequirements(ShooterSubsystem.getInstance());
    }

    @Override
    public void execute() {
        ShooterSubsystem.getInstance().setShooterState(ShooterState.Amp);
    }

    @Override
    public void end(boolean interrupted) {
        ShooterSubsystem.getInstance().setShooterState(ShooterState.off);
    }
}
