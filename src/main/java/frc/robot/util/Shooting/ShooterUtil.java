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
            case subwoofer -> {
                return new ShooterState(3500, 3500, MechanismState.deployed);
            }
            case tape , none -> {
                return new ShooterState(4000, 2000, MechanismState.stored);
            }
        }
        return null;
    }
}
