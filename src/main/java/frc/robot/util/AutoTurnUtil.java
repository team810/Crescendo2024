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
}

