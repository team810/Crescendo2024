package frc.robot.util.Shooting;

import edu.wpi.first.math.geometry.Pose2d;
import frc.lib.MechanismState;
import frc.robot.util.Rectangles.ShooterRectangle;

public class ShooterUtil {
    static ShootingZone zone;

    public static ShooterState calculateTargetSpeeds(Pose2d robotPose) {

        zone = ((ShooterRectangle) (ShooterUtilConstants.SHOOTING_ZONE_SET.findRectangle(robotPose))).getZone();

        return getStateAtRectangle(zone);
    }

    public static ShooterState getStateAtRectangle(ShootingZone rectangle) {
        switch (rectangle) {
            case topSub, botSub, midSub -> {
                return new ShooterState(4500, 2000, MechanismState.deployed);
            }
            case topTape, podium, midTape -> {
                return new ShooterState(2500, 2500, MechanismState.stored);
            }
            default -> {
                return new ShooterState(3000, 3000, MechanismState.stored);
            }
        }
    }
}
