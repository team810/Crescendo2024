package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.shooter.ShooterMode;
import frc.robot.subsystem.shooter.ShooterSubsystem;


public class RevShooterTestCommand extends Command {

    public RevShooterTestCommand() {
        addRequirements(ShooterSubsystem.getInstance());
    }

    @Override
    public void initialize() {
       ShooterSubsystem.getInstance().setShooterMode(ShooterMode.test);
    }

    @Override
    public void end(boolean interrupted) {
       ShooterSubsystem.getInstance().setShooterMode(ShooterMode.off);
    }
}
