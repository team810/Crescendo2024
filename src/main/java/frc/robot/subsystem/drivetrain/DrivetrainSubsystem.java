package frc.robot.subsystem.drivetrain;


import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.*;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import org.littletonrobotics.junction.Logger;

import static frc.robot.subsystem.drivetrain.DrivetrainConstants.*;


public class DrivetrainSubsystem extends SubsystemBase {
	private static final DrivetrainSubsystem INSTANCE = new DrivetrainSubsystem();
	public static DrivetrainSubsystem getInstance() {return INSTANCE;}

	private DrivetrainMode mode;

	private SwerveModulePosition frontLeftPosition;
	private SwerveModulePosition frontRightPosition;
	private SwerveModulePosition backLeftPosition;
	private SwerveModulePosition backRightPosition;

	private SwerveModuleState frontLeftState;
	private SwerveModuleState frontRightState;
	private SwerveModuleState backLeftState;
	private SwerveModuleState backRightState;

	private ChassisSpeeds targetSpeeds;
	private ChassisSpeeds telopSpeeds;
	private ChassisSpeeds trajectorySpeeds;

	private final SwerveModule frontLeft;
	private final SwerveModule frontRight;
	private final SwerveModule backLeft;
	private final SwerveModule backRight;

	private final SwerveDriveKinematics kinematics;
	private final SwerveDriveOdometry odometry;

	private final Pigeon2 gyro;

	private SpeedMode speedMode;

	private final PIDController thetaController;

	private final HolonomicDriveController driveController;

	private Trajectory.State trajectoryState;


	private DrivetrainSubsystem() {

		SwerveModuleDetails frontLeftDetails = new SwerveModuleDetails(FRONT_LEFT_MODULE_DRIVE_MOTOR, FRONT_LEFT_MODULE_STEER_MOTOR, FRONT_LEFT_MODULE_STEER_ENCODER, FRONT_LEFT_MODULE_STEER_OFFSET, SwerveModuleEnum.frontLeft);
		SwerveModuleDetails frontRightDetails = new SwerveModuleDetails(FRONT_RIGHT_MODULE_DRIVE_MOTOR, FRONT_RIGHT_MODULE_STEER_MOTOR, FRONT_RIGHT_MODULE_STEER_ENCODER, FRONT_RIGHT_MODULE_STEER_OFFSET, SwerveModuleEnum.frontRight);
		SwerveModuleDetails backLeftDetails = new SwerveModuleDetails(BACK_LEFT_MODULE_DRIVE_MOTOR, BACK_LEFT_MODULE_STEER_MOTOR, BACK_LEFT_MODULE_STEER_ENCODER, BACK_LEFT_MODULE_STEER_OFFSET, SwerveModuleEnum.backLeft);
		SwerveModuleDetails backRightDetails = new SwerveModuleDetails(BACK_RIGHT_MODULE_DRIVE_MOTOR, BACK_RIGHT_MODULE_STEER_MOTOR, BACK_RIGHT_MODULE_STEER_ENCODER, BACK_RIGHT_MODULE_STEER_OFFSET, SwerveModuleEnum.backRight);

		frontLeft = new SwerveModule(frontLeftDetails);
		frontRight = new SwerveModule(frontRightDetails);
		backLeft = new SwerveModule(backLeftDetails);
		backRight = new SwerveModule(backRightDetails);

		gyro = new Pigeon2(21);

		frontLeftPosition = frontLeft.getModulePosition();
		frontRightPosition = frontRight.getModulePosition();
		backLeftPosition = backLeft.getModulePosition();
		backRightPosition = backRight.getModulePosition();

		frontLeftState = new SwerveModuleState();
		frontRightState = new SwerveModuleState();
		backLeftState = new SwerveModuleState();
		backRightState = new SwerveModuleState();

		targetSpeeds = new ChassisSpeeds();
		telopSpeeds = new ChassisSpeeds();
		trajectorySpeeds = new ChassisSpeeds();

		kinematics = KINEMATICS;
		odometry = new SwerveDriveOdometry(kinematics, getRotation(), new SwerveModulePosition[]{frontLeftPosition, frontRightPosition, backLeftPosition, backRightPosition});
		mode = DrivetrainMode.teleop;

		setSpeedMode(SpeedMode.normal);

		thetaController = new PIDController(0,0,0);
		if (Robot.isReal())
		{
			thetaController.setP(THETA_CONTROLLER_REAL.kP);
			thetaController.setI(THETA_CONTROLLER_REAL.kI);
			thetaController.setD(THETA_CONTROLLER_REAL.kD);
		}else{
			thetaController.setP(THETA_CONTROLLER_SIM.kP);
			thetaController.setI(THETA_CONTROLLER_SIM.kI);
			thetaController.setD(THETA_CONTROLLER_SIM.kD);
		}

		thetaController.enableContinuousInput(-Math.PI, Math.PI);
		thetaController.setTolerance(.01);

		driveController = new HolonomicDriveController(
				new PIDController(6.5,0,0),
				new PIDController(6.5,0,0),
				new ProfiledPIDController(1.2,0,0,new TrapezoidProfile.Constraints(Math.PI * 4,Math.PI * 2))
		);


		driveController.setTolerance(new Pose2d(.05,.05, new Rotation2d(.01)));
		driveController.setEnabled(true);

		trajectoryState = new Trajectory.State();

	}

	@Override
	public void periodic() {

		if (RobotState.isDisabled())
		{
			targetSpeeds = new ChassisSpeeds(0,0,0);
		}

		SwerveModuleState[] states;
		switch (mode)
		{
            case teleop -> {
				targetSpeeds = telopSpeeds;
            }
            case trajectory -> {
				trajectorySpeeds = driveController.calculate(
						getPose(),
						trajectoryState,
						trajectoryState.poseMeters.getRotation()
				);
				trajectorySpeeds.vxMetersPerSecond = MathUtil.clamp(trajectorySpeeds.vxMetersPerSecond, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
				trajectorySpeeds.vyMetersPerSecond = MathUtil.clamp(trajectorySpeeds.vyMetersPerSecond, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
				trajectorySpeeds.omegaRadiansPerSecond = MathUtil.clamp(trajectorySpeeds.omegaRadiansPerSecond, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
				targetSpeeds = trajectorySpeeds;
			}
			default -> {
				targetSpeeds = new ChassisSpeeds(0,0,0);
			}
        }

		states = kinematics.toSwerveModuleStates(
				ChassisSpeeds.fromFieldRelativeSpeeds(targetSpeeds, getRotation())
		);

		// This mess with the pid controllers, it makes the mid controllers go back and forth
		states[0] = SwerveModuleState.optimize(states[0], frontLeft.getState().angle);
		states[1] = SwerveModuleState.optimize(states[1], frontRight.getState().angle);
		states[2] = SwerveModuleState.optimize(states[2], backLeft.getState().angle);
		states[3] = SwerveModuleState.optimize(states[3], backRight.getState().angle);

		frontLeft.setState(states[0]);
		frontRight.setState(states[1]);
		backLeft.setState(states[2]);
		backRight.setState(states[3]);

		// Commented out for testing purposes
		frontLeftState = states[0];
		frontRightState = states[1];
		backLeftState = states[2];
		backRightState = states[3];

		frontLeft.periodic();
		frontRight.periodic();
		backLeft.periodic();
		backRight.periodic();

		frontLeftPosition = frontLeft.getModulePosition();
		frontRightPosition = frontRight.getModulePosition();
		backLeftPosition = backLeft.getModulePosition();
		backRightPosition = backRight.getModulePosition();


		odometry.update(getRotation(), new SwerveModulePosition[]{frontLeftPosition, frontRightPosition, backLeftPosition, backRightPosition});

		Logger.recordOutput("Drivetrain/currentStates", frontLeft.getState(), frontRight.getState(), backLeft.getState(), backRight.getState());
		Logger.recordOutput("Drivetrain/states", states);
		Logger.recordOutput("Drivetrain/gyro", getRotation());
		Logger.recordOutput("RobotPose", getPose());
		Logger.recordOutput("Drivetrain/mode", mode);

		if (Robot.isSimulation())
		{
			gyro.setYaw(getRotation().getDegrees() + (Math.toDegrees(targetSpeeds.omegaRadiansPerSecond) * Robot.defaultPeriodSecs));
		}

	}

	private ChassisSpeeds getRobotRelativeSpeeds()
	{
		return kinematics.toChassisSpeeds(frontLeft.getState(), frontRight.getState(), backLeft.getState(), backRight.getState());
	}

	public void resetOdometry(Pose2d newPose)
	{
		frontLeftPosition = frontLeft.getModulePosition();
		frontRightPosition = frontRight.getModulePosition();
		backLeftPosition = backLeft.getModulePosition();
		backRightPosition = backRight.getModulePosition();

		odometry.resetPosition(getRotation(),new SwerveModulePosition[] {frontLeftPosition, frontRightPosition, backLeftPosition, backRightPosition}, newPose);
	}

	public DrivetrainMode getMode() {
		return mode;
	}
	public Rotation2d getRotation()
	{
		return gyro.getRotation2d();
	}
	public void zeroGyro()
	{
		gyro.reset();
	}
	public void setMode(DrivetrainMode mode) {
		this.mode = mode;
	}

	/**
	 * @param x this is the x input
	 * @param y this is the y input
	 * @param z rotate input
	 * This should get values that have already been altered and changed on a scale of -Max speed to Max speed
	 */
	public void setTelopSpeeds(double x, double y, double z)
	{
		targetSpeeds = new ChassisSpeeds(y,x,z);
	}

	public Pose2d getPose()
	{
		return odometry.getPoseMeters();
	}


	public PIDController getThetaController() { return thetaController; }

	public void setSpeedMode(SpeedMode speedMode) {
		this.speedMode = speedMode;

		frontLeft.setSpeedMode(this.speedMode);
		frontRight.setSpeedMode(this.speedMode);
		backLeft.setSpeedMode(this.speedMode);
		backRight.setSpeedMode(this.speedMode);
	}

	public SpeedMode getSpeedMode() {
		return speedMode;
	}

	public ChassisSpeeds getTargetSpeeds() {
		return targetSpeeds;
	}

	public Trajectory.State getTrajectoryState() {
		return trajectoryState;
	}

	public void setTrajectoryState(Trajectory.State trajectoryState) {
		this.trajectoryState = trajectoryState;
	}

	public boolean getDriveControllerAtSetpoint()
	{
		return driveController.atReference();
	}
}
