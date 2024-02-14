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
            vision = new VisionSim();
        } else {
            throw new RuntimeException("WHAT DID YOU DO");
        }
    }

    public double getAngle() {
        return vision.getRobotPosition().getRotation().getDegrees();
    }

    @Override
    public void periodic() {

        vision.updatePoseEstimation();

        Logger.recordOutput("Vision/robotX",
                vision.getRobotPosition().getX());
        Logger.recordOutput("Vision/robotY",
                vision.getRobotPosition().getY());
        Logger.recordOutput("Vision/robotTheta",
                vision.getRobotPosition().getRotation().getDegrees());
        Logger.recordOutput("Vision/robotPose",
                vision.getRobotPosition());

        if (!((vision.getRobotPosition().getX() == 0)
                && (vision.getRobotPosition().getY() == 0))) {
            DrivetrainSubsystem.getInstance().resetOdometry(
                    vision.getRobotPosition()
            );
        }
    }
}


