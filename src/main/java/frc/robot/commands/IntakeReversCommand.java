package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.intake.IntakeStates;
import frc.robot.subsystem.intake.IntakeSubsystem;


public class IntakeReversCommand extends Command {

    public IntakeReversCommand() {

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
