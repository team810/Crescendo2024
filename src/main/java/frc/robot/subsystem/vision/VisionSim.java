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


//        for (int i = 0; i < 16; i++) {
//
//            if (layout.getTagPose(i + 1).isPresent()) {
//
//                targetSim[i] = new VisionTargetSim(
//                        layout.getTagPose(i + 1).get(),
//                        new TargetModel(0.17, 0.17),
//                        i + 1
//                );
//            } else {
//                targetSim[i] = new VisionTargetSim(
//                        new Pose3d(new Translation3d(0, 0, 0),
//                                new Rotation3d(0, 0, 0)),
//                        new TargetModel(0.17, 0.17),
//                        i + 1
//                );
//            }
//        }

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
