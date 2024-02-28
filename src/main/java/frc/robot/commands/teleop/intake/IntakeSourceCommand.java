package frc.robot.commands.teleop.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.MechanismState;
import frc.robot.subsystem.deflector.DeflectorSubsystem;
import frc.robot.subsystem.intake.IntakeStates;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.shooter.ShooterMode;
import frc.robot.subsystem.shooter.ShooterSubsystem;


public class IntakeSourceCommand extends Command {

    public IntakeSourceCommand() {
        addRequirements(ShooterSubsystem.getInstance(), IntakeSubsystem.getInstance(), DeflectorSubsystem.getInstance());
    }

    @Override
    public void initialize() {
        DeflectorSubsystem.getInstance().setDeflectorState(MechanismState.deployed);
        IntakeSubsystem.getInstance().setState(IntakeStates.off);
        ShooterSubsystem.getInstance().setShooterMode(ShooterMode.SourceIntake);
    }

    @Override
    public void end(boolean interrupted) {
        IntakeSubsystem.getInstance().setState(IntakeStates.off);
        DeflectorSubsystem.getInstance().setDeflectorState(MechanismState.stored);
        ShooterSubsystem.getInstance().setShooterMode(ShooterMode.off);
    }
}
