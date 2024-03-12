package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.MechanismState;
import frc.robot.subsystem.deflector.DeflectorSubsystem;
import frc.robot.subsystem.shooter.ShooterMode;
import frc.robot.subsystem.shooter.ShooterSubsystem;


public class RevTapeCommand extends Command {

    public RevTapeCommand() {

        addRequirements(ShooterSubsystem.getInstance(), DeflectorSubsystem.getInstance());
    }

    @Override
    public void initialize() {
        ShooterSubsystem.getInstance().setShooterMode(ShooterMode.Tape);
        DeflectorSubsystem.getInstance().setDeflectorState(MechanismState.stored);
    }



    @Override
    public void end(boolean interrupted) {
        ShooterSubsystem.getInstance().setShooterMode(ShooterMode.off);
        DeflectorSubsystem.getInstance().setDeflectorState(MechanismState.stored);
    }
}
