package frc.robot.commands.score;

import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.MechanismState;
import frc.robot.subsystem.deflector.DeflectorSubsystem;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import frc.robot.subsystem.shooter.ShooterMode;
import frc.robot.subsystem.shooter.ShooterSubsystem;
import frc.robot.subsystem.tbone.TBoneSubsystem;
import frc.robot.util.Shooting.ShooterState;
import frc.robot.util.Shooting.ShooterUtil;


public class SpeakerScoreCommand extends Command {
    private ShooterState state;

    public SpeakerScoreCommand() {
        addRequirements(
                ShooterSubsystem.getInstance(),
                DeflectorSubsystem.getInstance(),
                DrivetrainSubsystem.getInstance(),
                TBoneSubsystem.getInstance()
        );
    }

    @Override
    public void initialize() {
        state = ShooterUtil.calculateTargetSpeeds(DrivetrainSubsystem.getInstance().getPose());

        ShooterSubsystem.getInstance().setSpeakerState(state);
        ShooterSubsystem.getInstance().setShooterMode(ShooterMode.Speaker);

        DeflectorSubsystem.getInstance().setDeflectorState(state.getDeflectorState());

        TBoneSubsystem.getInstance().setState(MechanismState.stored);
    }

    @Override
    public void execute() {
        state = ShooterUtil.calculateTargetSpeeds(DrivetrainSubsystem.getInstance().getPose());

        ShooterSubsystem.getInstance().setSpeakerState(state);
        ShooterSubsystem.getInstance().setShooterMode(ShooterMode.Speaker);

        DeflectorSubsystem.getInstance().setDeflectorState(state.getDeflectorState());

        TBoneSubsystem.getInstance().setState(MechanismState.stored);
    }

    @Override
    public void end(boolean interrupted) {
        ShooterSubsystem.getInstance().setShooterMode(ShooterMode.off);
        DeflectorSubsystem.getInstance().setDeflectorState(MechanismState.stored);
    }
}
