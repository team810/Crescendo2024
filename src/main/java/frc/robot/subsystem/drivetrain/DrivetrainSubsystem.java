package frc.robot.subsystem.drivetrain;


import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.navx.Navx;
import frc.lib.navx.NavxReal;
import frc.lib.navx.NavxSim;
import frc.robot.Robot;
import frc.robot.util.AutoTurn.AlignmentRectangle;
import frc.robot.util.AutoTurn.AutoTurnConstants;
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

	private ChassisSpeeds currentSpeeds;

	private ChassisSpeeds targetSpeeds;

	private final SwerveModule frontLeft;
	private final SwerveModule frontRight;
	private final SwerveModule backLeft;
	private final SwerveModule backRight;

	private final SwerveDriveKinematics kinematics;
	private final SwerveDriveOdometry odometry;

	private final Navx navx;

	private SpeedMode speedMode;

	private final PIDController thetaController;
	private double targetAngle; // Rads
	private boolean rotateEnabled;

	private AlignmentRectangle currentRectangle;

	private ChassisSpeeds autoSpeeds;


	private DrivetrainSubsystem() {

		SwerveModuleDetails frontLeftDetails = new SwerveModuleDetails(FRONT_LEFT_MODULE_DRIVE_MOTOR, FRONT_LEFT_MODULE_STEER_MOTOR, FRONT_LEFT_MODULE_STEER_ENCODER, FRONT_LEFT_MODULE_STEER_OFFSET, SwerveModuleEnum.frontLeft);
		SwerveModuleDetails frontRightDetails = new SwerveModuleDetails(FRONT_RIGHT_MODULE_DRIVE_MOTOR, FRONT_RIGHT_MODULE_STEER_MOTOR, FRONT_RIGHT_MODULE_STEER_ENCODER, FRONT_RIGHT_MODULE_STEER_OFFSET, SwerveModuleEnum.frontRight);
		SwerveModuleDetails backLeftDetails = new SwerveModuleDetails(BACK_LEFT_MODULE_DRIVE_MOTOR, BACK_LEFT_MODULE_STEER_MOTOR, BACK_LEFT_MODULE_STEER_ENCODER, BACK_LEFT_MODULE_STEER_OFFSET, SwerveModuleEnum.backLeft);
		SwerveModuleDetails backRightDetails = new SwerveModuleDetails(BACK_RIGHT_MODULE_DRIVE_MOTOR, BACK_RIGHT_MODULE_STEER_MOTOR, BACK_RIGHT_MODULE_STEER_ENCODER, BACK_RIGHT_MODULE_STEER_OFFSET, SwerveModuleEnum.backRight);

		frontLeft = new SwerveModule(frontLeftDetails);
		frontRight = new SwerveModule(frontRightDetails);
		backLeft = new SwerveModule(backLeftDetails);
		backRight = new SwerveModule(backRightDetails);

		if (Robot.isSimulation())
		{
			navx = new NavxSim();
		}else{
			navx = new NavxReal();
		}

		navx.setOffset(-Math.PI/2);

		frontLeftPosition = frontLeft.getModulePosition();
		frontRightPosition = frontRight.getModulePosition();
		backLeftPosition = backLeft.getModulePosition();
		backRightPosition = backRight.getModulePosition();

		frontLeftState = new SwerveModuleState();
		frontRightState = new SwerveModuleState();
		backLeftState = new SwerveModuleState();
		backRightState = new SwerveModuleState();

		currentSpeeds = new ChassisSpeeds();

		targetSpeeds = new ChassisSpeeds();

		kinematics = KINEMATICS;
		odometry = new SwerveDriveOdometry(kinematics, navx.getRotation2d(), new SwerveModulePosition[]{frontLeftPosition, frontRightPosition, backLeftPosition, backRightPosition});
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

		rotateEnabled = false;
		targetAngle = 0;

		currentRectangle = AutoTurnConstants.nullRectangle;

		AutoBuilder.configureHolonomic(
				this::getPose,
				this::resetOdometry,
				this::getRobotRelativeSpeeds,
				this::setAutoSpeeds,
				new HolonomicPathFollowerConfig(
						new PIDConstants(1,0,0),
						new PIDConstants(1,0,0),
						4.6,
						0.4,
						new ReplanningConfig()
				),
				() -> {
					// Boolean supplier that controls when the path will be mirrored for the red alliance
					// This will flip the path being followed to the red side of the field.
					// THE ORIGIN WILL REMAIN ON THE BLUE SIDE

					var alliance = DriverStation.getAlliance();
					if (alliance.isPresent()) {
						return alliance.get() == DriverStation.Alliance.Red;
					}
					return false;
				},
				this
		);
		setAutoSpeeds(new ChassisSpeeds());
	}

	@Override
	public void periodic() {

		currentRectangle = AutoTurnConstants.rectangleSet.findRectangle(getPose());

		if (RobotState.isDisabled())
		{
			targetSpeeds = new ChassisSpeeds(0,0,0);
		}


		SwerveModuleState[] states;
		if (RobotState.isTeleop())
		{
			states = kinematics.toSwerveModuleStates(
					ChassisSpeeds.fromFieldRelativeSpeeds(targetSpeeds, getRotation())
			);
		} else if (RobotState.isAutonomous()) {
			targetSpeeds = getAutoSpeeds();
			states = kinematics.toSwerveModuleStates(targetSpeeds);
		}else{
			targetSpeeds = new ChassisSpeeds(0,0,0);
			states = kinematics.toSwerveModuleStates(
					ChassisSpeeds.fromFieldRelativeSpeeds(targetSpeeds, getRotation())
			);
		}

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

		currentSpeeds = targetSpeeds;

		if (Robot.isSimulation())
		{
			// gyro update
			double gyroRate = currentSpeeds.omegaRadiansPerSecond;
			navx.setRate(gyroRate);

			navx.update(Robot.defaultPeriodSecs);
		}

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
		Logger.recordOutput("Drivetrain/states", frontLeftState, frontRightState, backLeftState, backRightState);
		Logger.recordOutput("Drivetrain/gyro", getRotation().getDegrees());
		Logger.recordOutput("Drivetrain/targetAngle", this.targetAngle);
		Logger.recordOutput("RobotPose", getPose());
		Logger.recordOutput("currentRectangle", currentRectangle.getName());
		navx.update(0);
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
		return navx.getRotation2d();
	}
	public void zeroGyro()
	{
		navx.zeroYaw();
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
	public void setTargetSpeeds(double x, double y, double z)
	{
		targetSpeeds = new ChassisSpeeds(y,x,z);
	}

	public void setTargetSpeeds(ChassisSpeeds speeds) {
		targetSpeeds = speeds;
	}

	public Pose2d getPose()
	{
		return odometry.getPoseMeters();
	}

	public Navx getNavx() {
		return navx;
	}
	public PIDController getThetaController() { return thetaController; }
	public AlignmentRectangle getCurrentRectangle() { return currentRectangle; }

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

	public double getTargetAngle() {
		return targetAngle;
	}

	public ChassisSpeeds getTargetSpeeds() {
		return targetSpeeds;
	}

	public void setTargetAngle(double targetAngle) {
		this.targetAngle = targetAngle;
	}
	public boolean isRotateEnabled() {
		return rotateEnabled;
	}

	public void setRotateEnabled(boolean rotateEnabled) {
		this.rotateEnabled = rotateEnabled;
	}

	public void setAutoSpeeds(ChassisSpeeds autoSpeeds) {
		this.autoSpeeds = autoSpeeds;
	}

	public ChassisSpeeds getAutoSpeeds() {
		return autoSpeeds;
	}
}
