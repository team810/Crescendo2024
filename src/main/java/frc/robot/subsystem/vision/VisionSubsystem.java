package frc.robot.subsystem.vision;


import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;
import org.photonvision.PhotonCamera;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.littletonrobotics.junction.Logger;

import java.io.IOException;

public class VisionSubsystem extends SubsystemBase {

    public static VisionSubsystem INSTANCE;
    public VisionReal vision;

    public static VisionSubsystem getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VisionSubsystem();
        }
        return INSTANCE;
    }

    private VisionSubsystem()
    {

        vision = new VisionReal();
    }

    @Override
    public void periodic() {
//        System.out.println("WE GOOD?");
        vision.updatePoseEstimation();

        Logger.recordOutput("Vision/robotX",
                vision.getRobotPosition().getX());
        Logger.recordOutput("Vision/robotY",
                vision.getRobotPosition().getY());
        Logger.recordOutput("Vision/robotTheta",
                vision.getRobotPosition().getRotation().getDegrees());

        DrivetrainSubsystem.getInstance().resetOdometry(
                vision.getRobotPosition()
        );
    }

}

