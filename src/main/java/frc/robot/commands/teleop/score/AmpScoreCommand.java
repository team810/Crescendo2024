package frc.robot.commands.teleop.score;

import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.MechanismState;
import frc.robot.subsystem.deflector.DeflectorSubsystem;
import frc.robot.subsystem.shooter.ShooterMode;
import frc.robot.subsystem.shooter.ShooterSubsystem;
import frc.robot.subsystem.tbone.TBoneSubsystem;


public class AmpScoreCommand extends Command {

    public AmpScoreCommand() {
        addRequirements(
                ShooterSubsystem.getInstance(),
                DeflectorSubsystem.getInstance(),
                TBoneSubsystem.getInstance()
        );
    }

    @Override
    public void initialize() {
        TBoneSubsystem.getInstance().setState(MechanismState.deployed);
        DeflectorSubsystem.getInstance().setDeflectorState(MechanismState.stored);
        ShooterSubsystem.getInstance().setShooterMode(ShooterMode.Amp);
    }

    @Override
    public void end(boolean interrupted) {
        TBoneSubsystem.getInstance().setState(MechanismState.stored);
        DeflectorSubsystem.getInstance().setDeflectorState(MechanismState.stored);
        ShooterSubsystem.getInstance().setShooterMode(ShooterMode.off);
    }
}
