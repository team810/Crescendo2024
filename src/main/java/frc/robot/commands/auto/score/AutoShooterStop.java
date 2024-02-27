package frc.robot.commands.auto.score;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.intake.IntakeStates;
import frc.robot.subsystem.intake.IntakeSubsystem;
import frc.robot.subsystem.shooter.ShooterMode;
import frc.robot.subsystem.shooter.ShooterSubsystem;
import frc.robot.util.Shooting.ShooterUtil;
import frc.robot.util.Shooting.ShooterUtilConstants;
import frc.robot.util.Shooting.ShootingZone;


public class AutoShooterStop extends Command {

   public AutoShooterStop() {
      // each subsystem used by the command must be passed into the
      // addRequirements() method (which takes a vararg of Subsystem)
      addRequirements(ShooterSubsystem.getInstance(), IntakeSubsystem.getInstance());
   }

   @Override
   public void initialize() {
      ShooterSubsystem.getInstance().setSpeakerState(
              ShooterUtil.getStateAtRectangle(ShootingZone.none)
      );

      ShooterSubsystem.getInstance().setShooterMode(ShooterMode.off);

      IntakeSubsystem.getInstance().setState(IntakeStates.off);
   }
}
