package frc.robot.util.Shooting;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import frc.lib.LinearInterpolation;
import frc.robot.util.AutoTurn.AutoTurnConstants;
import frc.robot.util.Rectangles.AlignmentRectangle;
import frc.robot.util.Rectangles.ShooterRectangle;

import java.util.ArrayList;

public class ShooterUtil {

    static ShootingZone zone;

    public Pair<Double, Double> calculateTargetSpeeds(Pose2d robotPose) {

        zone = ((ShooterRectangle) (AutoTurnConstants.RECTANGLE_SET.findRectangle(robotPose))).getZone();

        switch (zone) {
            case topSub -> {return new Pair<Double, Double>(0.0, 0.0);}
            case midSub -> {return new Pair<Double, Double>(0.0, 0.0);}
            case botSub -> {return new Pair<Double, Double>(0.0, 0.0);}
            case topTape -> {return new Pair<Double, Double>(0.0, 0.0);}
            case midTape -> {return new Pair<Double, Double>(0.0, 0.0);}
            case podium -> {return new Pair<Double, Double>(0.0, 0.0);}
            default -> {return new Pair<Double, Double>(0.0, 0.0);}
        }

    }
}
