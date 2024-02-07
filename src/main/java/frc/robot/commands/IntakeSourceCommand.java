package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.MechanismState;
import frc.robot.subsystem.intake.IntakeStates;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.shooter.ShooterMode;
import frc.robot.subsystem.shooter.ShooterSubsystem;


public class IntakeSourceCommand extends Command {

    public IntakeSourceCommand() {

        addRequirements(ShooterSubsystem.getInstance(), IntakeSubsystem.getInstance());
    }

    @Override
    public void initialize() {
        ShooterSubsystem.getInstance().setShooterState(ShooterMode.SourceIntake);
        ShooterSubsystem.getInstance().setDeflectorState(MechanismState.deployed);
        IntakeSubsystem.getInstance().setState(IntakeStates.rev);
    }

    @Override
    public boolean isFinished() {
        return IntakeSubsystem.getInstance().isLimitSwitchTriggered();
    }

    @Override
    public void end(boolean interrupted) {
        ShooterSubsystem.getInstance().setShooterState(ShooterMode.off);
        ShooterSubsystem.getInstance().setDeflectorState(MechanismState.stored);
        IntakeSubsystem.getInstance().setState(IntakeStates.off);
    }
}
