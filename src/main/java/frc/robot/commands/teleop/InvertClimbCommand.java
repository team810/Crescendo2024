package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.climber.ClimberStates;
import frc.robot.subsystem.climber.ClimberSubsystem;


public class InvertClimbCommand extends Command {

    public InvertClimbCommand() {

        addRequirements(ClimberSubsystem.getInstance());
    }

    @Override
    public void initialize() {
        ClimberSubsystem.getInstance().setState(ClimberStates.up);
    }


    @Override
    public void end(boolean interrupted) {
        ClimberSubsystem.getInstance().setState(ClimberStates.off);
    }
}
