package frc.robot.subsystem.vision;

import edu.wpi.first.math.geometry.Pose2d;

public interface VisionIO {

    void updatePoseEstimation();

    Pose2d getRobotPosition();
}
