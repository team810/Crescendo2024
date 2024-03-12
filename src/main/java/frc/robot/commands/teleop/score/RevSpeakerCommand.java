package frc.robot.commands.teleop.score;

import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.MechanismState;
import frc.robot.subsystem.deflector.DeflectorSubsystem;
import frc.robot.subsystem.shooter.ShooterMode;
import frc.robot.subsystem.shooter.ShooterSubsystem;


public class RevSpeakerCommand extends Command {

    public RevSpeakerCommand() {

        addRequirements(DeflectorSubsystem.getInstance(), ShooterSubsystem.getInstance());
    }

    @Override
    public void initialize() {
        ShooterSubsystem.getInstance().setShooterMode(ShooterMode.Subwoofer);
        DeflectorSubsystem.getInstance().setDeflectorState(MechanismState.deployed);
    }

    @Override
    public void end(boolean interrupted) {
        ShooterSubsystem.getInstance().setShooterMode(ShooterMode.off);
        DeflectorSubsystem.getInstance().setDeflectorState(MechanismState.stored);
    }
}
