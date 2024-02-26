package frc.robot.util.Shooting;

import edu.wpi.first.math.geometry.Pose2d;
import frc.lib.MechanismState;
import frc.robot.util.Rectangles.ShooterRectangle;

public class ShooterUtil {
    static ShootingZone zone;
    public static ShooterState calculateTargetSpeeds(Pose2d robotPose) {

        zone = ((ShooterRectangle) (ShooterUtilConstants.SHOOTING_ZONE_SET.findRectangle(robotPose))).getZone();

        switch (zone) {
            case topSub, botSub, midSub -> {return new ShooterState(4500,2000, MechanismState.deployed);}
            case topTape -> {return new ShooterState(400,400, MechanismState.stored);}
            case midTape -> {return new ShooterState(500,500, MechanismState.stored);}
            case podium -> {return new ShooterState(600,600, MechanismState.stored);}
            default -> {
                return new ShooterState(3000,3000,MechanismState.stored);
            }
        }

    }
}
