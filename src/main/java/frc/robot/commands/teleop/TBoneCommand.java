package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.MechanismState;
import frc.robot.subsystem.tbone.TBoneSubsystem;


public class TBoneCommand extends Command {

    public TBoneCommand() {
        addRequirements(TBoneSubsystem.getInstance());
    }

    @Override
    public void initialize() {
        TBoneSubsystem.getInstance().toggleState();
    }

    @Override
    public void end(boolean interrupted) {

    }
}
