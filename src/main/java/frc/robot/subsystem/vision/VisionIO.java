package frc.robot.subsystem.vision;

import edu.wpi.first.math.geometry.Pose2d;
import org.photonvision.targeting.PhotonTrackedTarget;

public interface VisionIO {

    void updatePoseEstimation();

    Pose2d getRobotPosition();
}
