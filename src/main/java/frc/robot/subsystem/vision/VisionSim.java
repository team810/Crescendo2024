package frc.robot.subsystem.vision;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.*;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import org.opencv.video.Video;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonTargetSortMode;
import org.photonvision.estimation.TargetModel;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.VisionSystemSim;
import org.photonvision.simulation.VisionTargetSim;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VideoSimUtil;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class VisionSim implements VisionIO {

    private PhotonCamera limelight;
    private PhotonCameraSim limelightSim;

    private SimCameraProperties cameraProperties;
    private AprilTagFieldLayout layout;

    private VisionSystemSim systemSim;

    public VisionSim() {

        limelight = new PhotonCamera("limelight");

        systemSim = new VisionSystemSim("LIMELIGHT SIM");

        cameraProperties = new SimCameraProperties();

//        cameraProperties.setFPS(90);

//        cameraProperties.setCalibration(320, 280, Rotation2d.fromDegrees(180));

        cameraProperties.setCalibError(0.25, 0.08);

        cameraProperties.setAvgLatencyMs(25);

        limelightSim = new PhotonCameraSim(limelight, cameraProperties);

        try {
            layout = AprilTagFieldLayout.loadFromResource(
                    AprilTagFields.k2024Crescendo.m_resourceFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        limelightSim.enableRawStream(true);

        limelightSim.enableProcessedStream(true);

        limelightSim.enableDrawWireframe(true);

        limelightSim.setMaxSightRange(3.5);

        systemSim.addCamera(limelightSim, VisionConstants.robotToCam);

        systemSim.addAprilTags(layout);
    }

    @Override
    public PhotonTrackedTarget getTarget() {
        return null;
    }

    @Override
    public void updatePoseEstimation() {

        systemSim.update(DrivetrainSubsystem.getInstance().getPose());
    }

    @Override
    public Pose2d getRobotPosition() {
        return systemSim.getRobotPose().toPose2d();
    }
}
