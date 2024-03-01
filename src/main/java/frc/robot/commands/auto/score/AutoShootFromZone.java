package frc.robot.commands.auto.score;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystem.shooter.ShooterMode;
import frc.robot.subsystem.shooter.ShooterSubsystem;
import frc.robot.util.Shooting.ShooterUtil;
import frc.robot.util.Shooting.ShootingZone;


public class AutoShootFromZone extends Command {

    private ShootingZone zone;

    public AutoShootFromZone(ShootingZone zone) {
        // each subsystem used by the command must be passed into the
        // addRequirements() method (which takes a vararg of Subsystem)

        this.zone = zone;
        addRequirements(ShooterSubsystem.getInstance());
    }

    @Override
    public void initialize() {
        ShooterSubsystem.getInstance().setSpeakerState(
                ShooterUtil.getStateAtRectangle(zone)
        );

        ShooterSubsystem.getInstance().setShooterMode(ShooterMode.Speaker);
    }
}
