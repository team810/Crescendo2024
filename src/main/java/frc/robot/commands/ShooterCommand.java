package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.shooter.ShooterSubsystem;


public class ShooterCommand extends Command {

    public ShooterCommand() {

        addRequirements(ShooterSubsystem.getInstance());
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        // TODO: Make this return true when this Command no longer needs to run execute()
        return false;
    }

    @Override
    public void end(boolean interrupted) {

    }
}
