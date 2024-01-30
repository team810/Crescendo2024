package frc.robot.subsystem.vision;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class VisionReal implements VisionIO {

    private final PhotonCamera limelight;
    private PhotonPipelineResult result;

    private PhotonPoseEstimator estimator;

    private Optional<EstimatedRobotPose> poseEstimation;
    private AprilTagFieldLayout layout;

    public VisionReal() {
        limelight = new PhotonCamera("limelight");
        limelight.setLED(VisionLEDMode.kOff);
        limelight.setPipelineIndex(0);

//        result = null;

        try {
            layout = AprilTagFieldLayout.loadFromResource(
                    AprilTagFields.k2024Crescendo.m_resourceFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        estimator = new PhotonPoseEstimator(layout,
                PhotonPoseEstimator.PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
                limelight,
                VisionConstants.robotToCam);

    }

    public PhotonTrackedTarget getTarget() {
        result = limelight.getLatestResult();
        if (result.hasTargets()) {
            return result.getBestTarget();
        } else {
            return null;
        }
    }

    public void updatePoseEstimation() {
            poseEstimation = estimator.update();
    }

    public Pose2d getRobotPosition() {
        if (poseEstimation.isPresent()) {
            return new Pose2d(
                    new Translation2d(poseEstimation.get().estimatedPose.getX(),
                            poseEstimation.get().estimatedPose.getY()),
                    new Rotation2d(poseEstimation.get().estimatedPose.
                            getRotation().getAngle())
            );
        }  else {
//            System.out.println("NO TARGET!!!");
            return new Pose2d(new Translation2d(0, 0),
                    new Rotation2d(0));
        }
    }
}
