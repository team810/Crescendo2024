package frc.robot.util;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public class AutoTurnUtil {

    /**
     * @param robotPose
     * @return The angle that is returned is constrained between -PI and PI
     */
    public static Rotation2d calculateTargetAngle(Pose2d robotPose)
    {
        return new Rotation2d(Math.toRadians(90));
    }

    private boolean inRectangle(Pose2d currentPos, double leftX, double rightX,
                                double bottomY, double topY) {

        double x = currentPos.getX();
        double y = currentPos.getY();

        return ((x >= leftX) && (x <= rightX) && (y > bottomY) && (y < topY));
    }
}

