package frc.robot.commands.auto.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.intake.IntakeStates;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.laser.LaserState;
import frc.robot.subsystem.laser.LaserSubsystem;


public class AutoIntakeWithLaserCommand extends Command {

    public AutoIntakeWithLaserCommand() {

        addRequirements(IntakeSubsystem.getInstance(), LaserSubsystem.getInstance());
    }

    @Override
    public void initialize() {
        IntakeSubsystem.getInstance().setState(IntakeStates.off);
    }

    @Override
    public void execute() {
        IntakeSubsystem.getInstance().setState(IntakeStates.fwd);
    }

    @Override
    public boolean isFinished() {
        return LaserSubsystem.getInstance().getLaserState() == LaserState.Detected;
    }

    @Override
    public void end(boolean interrupted) {
        IntakeSubsystem.getInstance().setState(IntakeStates.off);
    }
}
