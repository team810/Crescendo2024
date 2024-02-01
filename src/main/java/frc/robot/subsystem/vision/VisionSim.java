package frc.robot.subsystem.vision;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.*;
import org.photonvision.PhotonCamera;
import org.photonvision.estimation.TargetModel;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.VisionSystemSim;
import org.photonvision.simulation.VisionTargetSim;
import org.photonvision.targeting.PhotonTrackedTarget;

import java.io.IOException;

public class VisionSim implements VisionIO {

    private PhotonCameraSim limelightSim;
//    private VisionTargetSim[] targetSim = new VisionTargetSim[16];
    private AprilTagFieldLayout layout;

    private VisionSystemSim systemSim;

    public VisionSim() {

        systemSim = new VisionSystemSim("LIMELIGHT SIM");

        limelightSim = new PhotonCameraSim(new PhotonCamera("limelight"));

        try {
            layout = AprilTagFieldLayout.loadFromResource(
                    AprilTagFields.k2024Crescendo.m_resourceFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        systemSim.addAprilTags(layout);

        systemSim.addCamera(limelightSim, VisionConstants.robotToCam);
    }

    @Override
    public PhotonTrackedTarget getTarget() {
        return null;
    }

    @Override
    public void updateTargetData(double[] data) {

    }

    @Override
    public void updatePoseEstimation() {

    }

    @Override
    public Pose2d getRobotPosition() {
        return null;
    }
}
