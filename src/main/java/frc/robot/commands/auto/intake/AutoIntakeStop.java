package frc.robot.commands.auto.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.intake.IntakeStates;
import frc.robot.subsystem.intake.IntakeSubsystem;


public class AutoIntakeStop extends Command {

   public AutoIntakeStop() {
      // each subsystem used by the command must be passed into the
      // addRequirements() method (which takes a vararg of Subsystem)
      addRequirements(IntakeSubsystem.getInstance());
   }

   @Override
   public void initialize() {
      IntakeSubsystem.getInstance().setState(IntakeStates.off);
   }

   @Override
   public boolean isFinished() {
      return true;
   }

}
