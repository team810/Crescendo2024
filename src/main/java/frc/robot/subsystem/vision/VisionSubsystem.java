package frc.robot.subsystem.vision;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import org.littletonrobotics.junction.Logger;

public class VisionSubsystem extends SubsystemBase {

    public static VisionSubsystem INSTANCE;
    public VisionIO vision;

    public static VisionSubsystem getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VisionSubsystem();
        }
        return INSTANCE;
    }

    private VisionSubsystem() {

        if (Robot.isReal()) {
            vision = new VisionReal();
        } else if (Robot.isSimulation()) {
            vision = new VisionReal();
        } else {
            throw new RuntimeException("WHAT DID YOU DO");
        }

//        vision.updatePoseEstimation(DrivetrainSubsystem.getInstance().getPose());
    }

    @Override
    public void periodic() {

        Logger.recordOutput("Vision/estimatedX", vision.getRobotPosition().estimatedPose.getX());
        Logger.recordOutput("Vision/estimatedY", vision.getRobotPosition().estimatedPose.getY());
        Logger.recordOutput("Vision/estimatedTimestamp", vision.getRobotPosition().timestampSeconds);

        if ((vision.getRobotPosition().timestampSeconds != -1)) {
            Logger.recordOutput("Vision/estimatedPose2", vision.getRobotPosition().estimatedPose.toPose2d());

//            System.out.println("UPDATING POSE");
//            vision.updatePoseEstimation(vision.getRobotPosition().estimatedPose.toPose2d());
            DrivetrainSubsystem.getInstance().addVisionMeasurement(
                    vision.getRobotPosition()
            );
        }
    }
}


