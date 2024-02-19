package frc.robot.util.Shooting;

import edu.wpi.first.math.geometry.Pose2d;
import frc.lib.MechanismState;
import frc.robot.util.AutoTurn.AutoTurnConstants;
import frc.robot.util.Rectangles.ShooterRectangle;

public class ShooterUtil {
    public static ShooterState calculateTargetSpeeds(Pose2d robotPose) {
        ShootingZone zone;
        zone = ((ShooterRectangle) (AutoTurnConstants.RECTANGLE_SET.findRectangle(robotPose))).getZone();

        switch (zone) {
            case topSub -> {return new ShooterState(0,0, MechanismState.deployed);}
            case midSub -> {return new ShooterState(0,0, MechanismState.deployed);}
            case botSub -> {return new ShooterState(0,0, MechanismState.deployed);}
            case topTape -> {return new ShooterState(0,0, MechanismState.stored);}
            case midTape -> {return new ShooterState(0,0, MechanismState.stored);}
            case podium -> {return new ShooterState(0,0, MechanismState.stored);}
            default -> {
                return new ShooterState(3000,3000,MechanismState.stored);
            }
        }

    }
}
