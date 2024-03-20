package frc.robot.subsystem.vision;


import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonPoseEstimator;

public class VisionSubsystem extends SubsystemBase {

    public static VisionSubsystem INSTANCE;
    public VisionIO vision;

    private final AprilTagFieldLayout fieldLayout = AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();
    private final PhotonPoseEstimator estimator;
    private VisionSubsystem() {
        if (Robot.isReal()) {
            vision = new VisionReal();
        } else {
            vision = new VisionSim();
        }

        this.estimator = new PhotonPoseEstimator(fieldLayout, PhotonPoseEstimator.PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, VisionConstants.robotToCam);
    }

    @Override
    public void periodic() {
        vision.update();
        EstimatedRobotPose estimatedRobotPose = estimator.update(vision.getPipelineResults()).orElse(null);
        if (estimatedRobotPose != null)
        {
            DrivetrainSubsystem.getInstance().addVisionMeasurement(estimatedRobotPose);
        }


    }

    public static VisionSubsystem getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VisionSubsystem();
        }
        return INSTANCE;
    }
}


