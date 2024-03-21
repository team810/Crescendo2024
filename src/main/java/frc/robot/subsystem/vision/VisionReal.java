package frc.robot.subsystem.vision;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import org.littletonrobotics.junction.Logger;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.targeting.PhotonTrackedTarget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class VisionReal implements VisionIO {

    private final PhotonCamera limelight;
    private PhotonPoseEstimator estimator;
    private Optional<EstimatedRobotPose> poseEstimation;
    private AprilTagFieldLayout layout;

    public VisionReal() {
        limelight = new PhotonCamera("cam");
        
        limelight.setLED(VisionLEDMode.kOff);

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

    public void updatePoseEstimation(Pose2d prevEstimatedPose) {

        estimator.setReferencePose(prevEstimatedPose);

//        if (limelight.isConnected())
//        {
//            poseEstimation = estimator.update();
//        }
    }

    public EstimatedRobotPose getRobotPosition() {

        if (limelight.isConnected())
        {

            poseEstimation = estimator.update();
            if (poseEstimation.isPresent()) {
//                System.out.println("ESTIMATING. . . ");
                Logger.recordOutput("Vision/estimatedPose", poseEstimation.get().estimatedPose.toPose2d());

                return poseEstimation.get();
            }  else {

                return new EstimatedRobotPose(new Pose3d(), -1, new ArrayList<PhotonTrackedTarget>(),
                        PhotonPoseEstimator.PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR);
            }
        }else{

            return new EstimatedRobotPose(new Pose3d(), -1, new ArrayList<PhotonTrackedTarget>(),
                    PhotonPoseEstimator.PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR);
        }

    }
}
