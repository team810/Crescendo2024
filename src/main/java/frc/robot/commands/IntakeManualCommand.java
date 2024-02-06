package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.intake.IntakeStates;
import frc.robot.subsystem.intake.IntakeSubsystem;

import java.util.function.Supplier;


public class IntakeManualCommand extends Command {

    private final Supplier<Double> joystickValue;

    public IntakeManualCommand(Supplier<Double> joystickValue) {
        this.joystickValue = joystickValue;

        addRequirements();
    }

    @Override
    public void initialize() {
        IntakeSubsystem.getInstance().setState(IntakeStates.manual);
        IntakeSubsystem.getInstance().setManualSpeed(0);
    }

    @Override
    public void execute() {
        double input = MathUtil.applyDeadband(joystickValue.get(), .05);
        IntakeSubsystem.getInstance().setManualSpeed(input);
    }

    @Override
    public void end(boolean interrupted) {
        IntakeSubsystem.getInstance().setManualSpeed(0);
        IntakeSubsystem.getInstance().setState(IntakeStates.off);
    }
}
