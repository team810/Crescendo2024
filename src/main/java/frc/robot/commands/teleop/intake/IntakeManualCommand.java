package frc.robot.commands.teleop.intake;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.IO.Controls;
import frc.robot.IO.IO;
import frc.robot.subsystem.intake.IntakeStates;
import frc.robot.subsystem.intake.IntakeSubsystem;


public class IntakeManualCommand extends Command {

    public IntakeManualCommand() {

        addRequirements(IntakeSubsystem.getInstance());
    }

    @Override
    public void initialize() {
       IntakeSubsystem.getInstance().setState(IntakeStates.manual);
       IntakeSubsystem.getInstance().setManualSpeed(0);
    }

    @Override
    public void execute() {
       double speed;

       speed = IO.getJoystickValue(Controls.intakeManual).get();
       speed = MathUtil.applyDeadband(speed, .1);
       speed = Math.pow(speed, 3);

       IntakeSubsystem.getInstance().setManualSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
       IntakeSubsystem.getInstance().setManualSpeed(0);
       IntakeSubsystem.getInstance().setState(IntakeStates.off);
    }
}
