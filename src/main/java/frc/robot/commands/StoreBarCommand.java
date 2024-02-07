package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.shooter.BarState;
import frc.robot.subsystem.shooter.ShooterConstants;
import frc.robot.subsystem.shooter.ShooterSubsystem;


public class StoreBarCommand extends Command {

    private final Timer timer;

    public StoreBarCommand() {
        timer = new Timer();

        addRequirements(ShooterSubsystem.getInstance());
    }

    @Override
    public void initialize() {
        timer.start();
        ShooterSubsystem.getInstance().setBarState(BarState.revs);
    }

    @Override
    public boolean isFinished() {
        return (timer.hasElapsed(ShooterConstants.BAR_SECONDS)) ||
                (ShooterSubsystem.getInstance().getBarState() == BarState.stored);
    }

    @Override
    public void end(boolean interrupted) {
        ShooterSubsystem.getInstance().setBarState(BarState.stored);
        timer.stop();
        timer.reset();
    }
}
