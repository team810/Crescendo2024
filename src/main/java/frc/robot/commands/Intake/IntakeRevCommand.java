package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.intake.IntakeStates;
import frc.robot.subsystem.intake.IntakeSubsystem;


public class IntakeRevCommand extends Command {

    public IntakeRevCommand() {

        addRequirements(IntakeSubsystem.getInstance());
    }

    @Override
    public void initialize() {
        IntakeSubsystem.getInstance().setState(IntakeStates.rev);
    }

    @Override
    public void end(boolean interrupted) {
        IntakeSubsystem.getInstance().setState(IntakeStates.off);
    }
}
