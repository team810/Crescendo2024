package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.Deadband;
import frc.robot.IO.Controls;
import frc.robot.IO.IO;
import frc.robot.subsystem.climber.ClimberStates;
import frc.robot.subsystem.climber.ClimberSubsystem;
import frc.robot.subsystem.intake.IntakeStates;
import frc.robot.subsystem.intake.IntakeSubsystem;


public class ClimberCommand extends Command {

    public ClimberCommand() {
        addRequirements(ClimberSubsystem.getInstance());
    }

    @Override
    public void execute() {

        if (IO.getButtonValue(Controls.climberDown).get()) {

            ClimberSubsystem.getInstance().setState(ClimberStates.down);

        } else if (IO.getButtonValue(Controls.climberUp).get()) {

            ClimberSubsystem.getInstance().setState(ClimberStates.up);

        } else if (IO.getButtonValue(Controls.climberAdjUp).get()) {

            ClimberSubsystem.getInstance().setState(ClimberStates.manualUp);

        } else if (IO.getButtonValue(Controls.climberAdjDown).get()) {

            ClimberSubsystem.getInstance().setState(ClimberStates.manualDown);

        } else {

            ClimberSubsystem.getInstance().setState(ClimberStates.stopped);

        }

    }
}
