package frc.robot.util.AutoTurn;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.util.Rectangles.AlignmentRectangle;

public class AutoTurnUtil {

    static AutoTurnMode type;
    public static Rotation2d calculateTargetAngle(Pose2d robotPose) {
        type = ((AlignmentRectangle) (AutoTurnConstants.RECTANGLE_SET.findRectangle(robotPose))).getType();

        switch (type) {
            case amp:
                return new Rotation2d(Math.toRadians(90));
            case redSource:
                return new Rotation2d(Math.toRadians(120));
            case blueSource:
                return new Rotation2d(Math.toRadians(60));
            case blueSpeaker:
                return new Rotation2d(MathUtil.angleModulus(
                            Math.atan(
                                (5.60 - robotPose.getY()) / (robotPose.getX())))
                        );
            case redSpeaker:
                return new Rotation2d(MathUtil.angleModulus(
                        -Math.atan(
                                (5.60 - robotPose.getY()) / (16.50 - robotPose.getX())
                        ) + Math.PI)
                );
        }

        return new Rotation2d(Math.toRadians(90));
    }

    public static Rotation2d getSourceAngle() {

        if (DriverStation.getAlliance().get() == DriverStation.Alliance.Red) {
            return new Rotation2d(Math.toRadians(60));
        } else if (DriverStation.getAlliance().get() == DriverStation.Alliance.Blue) {
            return new Rotation2d(Math.toRadians(120));
        }

        return new Rotation2d(90);
    }

    public static Rotation2d getAmpAngle() {
        return new Rotation2d(Math.toRadians(-90));
    }

    public static Rotation2d getPodiumAngle() {
        if (DriverStation.getAlliance().get() == DriverStation.Alliance.Red) {
            return new Rotation2d(Math.toRadians(-150));
        } else if (DriverStation.getAlliance().get() == DriverStation.Alliance.Blue) {
            return new Rotation2d(Math.toRadians(-30));
        }
        return new Rotation2d(90);
    }
}

