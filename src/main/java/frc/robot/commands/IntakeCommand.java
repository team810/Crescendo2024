package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.Deadband;
import frc.robot.IO.Controls;
import frc.robot.IO.IO;
import frc.robot.subsystem.intake.IntakeStates;
import frc.robot.subsystem.intake.IntakeSubsystem;


public class IntakeCommand extends Command {

    private final Deadband intakeDeadband = new Deadband(0.1);

    public IntakeCommand() {
        addRequirements(IntakeSubsystem.getInstance());
    }

    @Override
    public void execute() {

        if (IO.getButtonValue(Controls.intakeFwd).get()) {
            IntakeSubsystem.getInstance().setState(IntakeStates.runForward);
        } else if (IO.getButtonValue(Controls.intakeRev).get()) {
            IntakeSubsystem.getInstance().setState(IntakeStates.runReverse);
        } else if (IO.getJoystickValue(Controls.manualIntake).get() != 0) {
            IntakeSubsystem.getInstance().setState(IntakeStates.manualInput);
            IntakeSubsystem.getInstance().setManualSpeed(
                    intakeDeadband.apply(IO.getJoystickValue(Controls.manualIntake).get())
            );
        } else { IntakeSubsystem.getInstance().setState(IntakeStates.stopped); }

    }
}
