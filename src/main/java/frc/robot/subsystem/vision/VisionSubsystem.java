package frc.robot.subsystem.vision;


import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.subsystem.drivetrain.DrivetrainSubsystem;

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
    }

    @Override
    public void periodic() {
        vision.updatePoseEstimation();
        if ((((vision.getRobotPosition().getX() != 0)) && (RobotState.isTeleop()))
                && (vision.getRobotPosition().getY() != 0)) {
            DrivetrainSubsystem.getInstance().resetOdometry(
                    vision.getRobotPosition()
            );
        }
    }
}


