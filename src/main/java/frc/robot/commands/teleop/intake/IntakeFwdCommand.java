package frc.robot.commands.teleop.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.intake.IntakeStates;
import frc.robot.subsystem.intake.IntakeSubsystem;


public class IntakeFwdCommand extends Command {

    public IntakeFwdCommand() {

        addRequirements(IntakeSubsystem.getInstance());
    }
    @Override
    public void initialize() {
        IntakeSubsystem.getInstance().setState(IntakeStates.fwd);
    }

    @Override
    public void end(boolean interrupted) {
        IntakeSubsystem.getInstance().setState(IntakeStates.off);
    }
}
