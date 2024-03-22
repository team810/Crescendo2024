package frc.robot.subsystem.drivetrain;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.RobotState;
import frc.robot.Robot;
import org.littletonrobotics.junction.Logger;

class SwerveModule {
    private final SwerveModuleIO module;
    private final PIDController steerController;

    private SwerveModuleState state;
    private SwerveModulePosition position;

    private final SwerveModuleDetails details;

    private final DriveControlMode driveControlMode;
    public SwerveModule(SwerveModuleDetails details)
    {
        steerController = new PIDController(0,0,0, Robot.defaultPeriodSecs);

        steerController.setTolerance(0);

        if (Robot.isReal())
        {
            steerController.setP(DrivetrainConstants.STEER_CONTROLLER_REAL.kP);
            steerController.setI(DrivetrainConstants.STEER_CONTROLLER_REAL.kI);
            steerController.setD(DrivetrainConstants.STEER_CONTROLLER_REAL.kD);

            steerController.enableContinuousInput(-Math.PI, Math.PI);
            steerController.setTolerance(.005);

            driveControlMode = DriveControlMode.Voltage;

        } else if (Robot.isSimulation()) {

            steerController.setP(DrivetrainConstants.STEER_CONTROLLER_SIM.kP);
            steerController.setI(DrivetrainConstants.STEER_CONTROLLER_SIM.kI);
            steerController.setD(DrivetrainConstants.DRIVE_CONTROLLER_SIM.kD);

            steerController.enableContinuousInput(-Math.PI, Math.PI);
            steerController.setTolerance(.005);

            driveControlMode = DriveControlMode.Voltage;
        }else{
            throw new RuntimeException(
                    "The PID controls for both the drive controller " +
                    "and the steer controllers are not getting configured, " +
                    "how is the robot not real or simulated"
            );
        }

        position = new SwerveModulePosition();

        state = new SwerveModuleState();

        if (Robot.isSimulation())
        {
            module = new SwerveModuleSim(details);
        }else {
            module = new SwerveModuleRev(details);
        }

        this.details = details;


        module.setState(new SwerveModuleState(0,new Rotation2d()));
    }

    public void setIdleMode(CANSparkMax.IdleMode mode)
    {
        module.setIdleMode(mode);
    }

    void periodic(){
        module.setState(state);


        if (driveControlMode == DriveControlMode.Velocity)
        {
            double speedOfMotorRPM =
                    (state.speedMetersPerSecond / DrivetrainConstants.DISTANCE_PER_REVOLUTION)
                            * 60 * DrivetrainConstants.GEAR_REDUCTION_DRIVE;
            module.setWheelTargetVelocity(speedOfMotorRPM);
            Logger.recordOutput("Drivetrain/" + details.module.name() + "/TargetVelocity", speedOfMotorRPM);
        }else{
            module.setDriveVoltage(state.speedMetersPerSecond / DrivetrainConstants.NORMAL_SPEED);
        }
        module.setSteerVoltage(
                steerController.calculate(module.getWheelAngle().getRadians(),
                        MathUtil.angleModulus(state.angle.getRadians()))
        );

        Logger.recordOutput("Drivetrain/" + details.module.name() +
                "/TargetAngle", state.angle.getRadians());
        Logger.recordOutput("Drivetrain/" + details.module.name() +
                "/AtAngleSetpoint", steerController.atSetpoint());


        module.update();
        position.distanceMeters = module.getWheelPosition();
        position.angle = module.getWheelAngle();

        if (RobotState.isDisabled())
        {
            steerController.reset();
        }
    }

    void setState(SwerveModuleState state)
    {
        this.state = state;
    }

    void resetModulePositions()
    {
        module.resetPosition();
        position = new SwerveModulePosition();
    }

    public SwerveModulePosition getModulePosition()
    {
//        if (Robot.isReal()) {
//            if (DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == DriverStation.Alliance.Red) {
//                position.distanceMeters = position.distanceMeters;
//            } else {position.distanceMeters = -position.distanceMeters;}
//        }
        position.distanceMeters = -position.distanceMeters;
        return position;
    }

    SwerveModuleState getState()
    {
        double speedOfWheel = module.getWheelVelocity();
        speedOfWheel = (((speedOfWheel / DrivetrainConstants.GEAR_REDUCTION_DRIVE) / 60))
                * DrivetrainConstants.DISTANCE_PER_REVOLUTION;

        return new SwerveModuleState(speedOfWheel, module.getWheelAngle());
    }

    enum DriveControlMode
    {
        Velocity,
        Voltage
    }
}
