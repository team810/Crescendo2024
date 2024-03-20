package frc.robot.subsystem.vision;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import org.photonvision.PhotonCamera;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VisionSystemSim;
import org.photonvision.targeting.PhotonPipelineResult;

import java.io.IOException;

public class VisionSim implements VisionIO {

    private final PhotonCamera limelight;
    private PhotonCameraSim limelightSim;

    private SimCameraProperties cameraProperties;
    private AprilTagFieldLayout layout;

    private VisionSystemSim systemSim;

    public VisionSim() {

        limelight = new PhotonCamera("limelight");

        systemSim = new VisionSystemSim("LIMELIGHT SIM");

        cameraProperties = new SimCameraProperties();

        cameraProperties.setFPS(70);

        cameraProperties.setCalibration(1280, 720, Rotation2d.fromDegrees(90));

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
    public void update() {
        systemSim.update(DrivetrainSubsystem.getInstance().getPose());
    }

    @Override
    public PhotonPipelineResult getPipelineResults() {
        return limelight.getLatestResult();
    }
}
