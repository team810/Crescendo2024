package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.intake.IntakeStates;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.laser.LaserState;
import frc.robot.subsystem.laser.LaserSubsystem;


public class IntakeFwdCommand extends Command {

    public IntakeFwdCommand() {

        addRequirements(IntakeSubsystem.getInstance());
    }
    @Override
    public void initialize() {
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
