package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.climber.ClimberStates;
import frc.robot.subsystem.climber.ClimberSubsystem;


public class ClimbCommand extends Command {

    public ClimbCommand() {

        addRequirements(ClimberSubsystem.getInstance());
    }

    @Override
    public void initialize() {
        ClimberSubsystem.getInstance().setState(ClimberStates.on);
    }


    @Override
    public void end(boolean interrupted) {
        ClimberSubsystem.getInstance().setState(ClimberStates.off);
    }
}
